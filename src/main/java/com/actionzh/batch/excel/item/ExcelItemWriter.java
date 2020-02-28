package com.actionzh.batch.excel.item;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.WriterNotOpenException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.util.ExecutionContextUserSupport;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @param <T>
 * @author Raghavender B
 */
public class ExcelItemWriter<T> extends ExecutionContextUserSupport implements ResourceAwareItemWriterItemStream<T>,
        InitializingBean {

    protected static final Logger logger = LoggerFactory.getLogger(ExcelItemWriter.class);

    private Resource resource;

    private boolean initialized = false;

    private Workbook workbook;

    private Font font;

    private CellStyle cellStyle;

    private CellStyle dateCellStyle;

    private String[] names;

    private String sheetName = "Untitled";
    /**
     * If this property is set , It indicates which field has Sheet name.
     * if not set, it writes in a single sheet with default sheet name i.e Untitled
     */
    private Integer indexOfSheetName;

    private Sheet sheet;

    private long linesWritten = 0;

    private int currentRowIndex = 0;

    private OutputStream outputStream;

    private boolean shouldDeleteIfEmpty = true;

    private ExcelHeaderCallback headerCallback;

    private ExcelFooterCallback footerCallback;

    private ExcelWorkbookFactory workbookFactory = new DefaultExcelWorkbookFactory();

    private FieldExtractor<T> fieldExtractor;

    private Map<String, Integer> sheetRowIndex = new HashMap<>();

    /**
     * Default date format we can specify other formats also.
     */
    private String dateFormat = "m/d/yy";

    public ExcelItemWriter() {
        setName(ClassUtils.getShortName(ExcelItemWriter.class));
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }


    /**
     * @param dateFormat the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @param indexOfSheetName the indexOfSheetName to set
     */
    public void setIndexOfSheetName(Integer indexOfSheetName) {
        this.indexOfSheetName = indexOfSheetName;
    }

    /**
     * headerCallback will be called before writing the first item to file.
     * Newline will be automatically appended after the header is written.
     */
    public void setHeaderCallback(ExcelHeaderCallback headerCallback) {
        this.headerCallback = headerCallback;
    }

    /**
     * footerCallback will be called after writing the last item to file, but
     * before the file is closed.
     */
    public void setFooterCallback(ExcelFooterCallback footerCallback) {
        this.footerCallback = footerCallback;
    }

    public void setShouldDeleteIfEmpty(boolean shouldDeleteIfEmpty) {
        this.shouldDeleteIfEmpty = shouldDeleteIfEmpty;
    }

    /**
     * @param workbookFactory the workbookFactory to set
     */
    public void setWorkbookFactory(ExcelWorkbookFactory workbookFactory) {
        this.workbookFactory = workbookFactory;
    }

    public void setFieldExtractor(FieldExtractor<T> fieldExtractor) {
        this.fieldExtractor = fieldExtractor;
    }


    @Override
    public void close() throws ItemStreamException {

        if (workbook == null) return;

        try {
            if (footerCallback != null) {
                footerCallback.writeFooter(createNextRow());
            }

            if (shouldDeleteIfEmpty && linesWritten == 0) {
                return;
            }

            try {
                outputStream = new FileOutputStream(resource.getFile().getAbsolutePath(), false);
                workbook.write(outputStream);
            } catch (IOException e) {
                throw new ItemStreamException("Failed to write workbook to file", e);
            }
        } finally {
            IOUtils.closeQuietly(outputStream);
            //reset
            initialized = false;
            linesWritten = 0;
            currentRowIndex = 0;
        }
    }

    @Override
    public void open(ExecutionContext executionContext)
            throws ItemStreamException {
        Assert.notNull(resource, "The resource must be set");
        if (!initialized) {
            doOpen(executionContext);
        }

    }

    protected void doOpen(ExecutionContext executionContext)
            throws ItemStreamException {

        try {
            File file = resource.getFile();
            FileUtils.setUpOutputFile(file, false, false, true);
            workbook = workbookFactory.create(resource);
            font = getDefaultFont();
            cellStyle = getDefaultCellStyle();
            dateCellStyle = getDefaultCellStyle();
            dateCellStyle.setDataFormat(workbook.createDataFormat().getFormat(dateFormat));
            initialized = true;
        } catch (IOException e) {
            throw new ItemStreamException("Failed to initialize writer", e);
        }

    }

    @Override
    public void update(ExecutionContext executionContext)
            throws ItemStreamException {
    }


    @Override
    public void write(List<? extends T> items) throws Exception {
        if (!initialized) {
            throw new WriterNotOpenException("Writer must be open before it can be written to");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Writing to excel file with " + items.size() + " items.");
        }

        Object cellValues[] = null;
        for (T item : items) {
            cellValues = fieldExtractor.extract(item);
            linesWritten++;

            if (indexOfSheetName != null) {
                sheetName = (String) cellValues[indexOfSheetName];
                cellValues = ArrayUtils.remove(cellValues, indexOfSheetName);
            }

            if (workbook.getSheet(sheetName) == null) {
                sheet = workbook.createSheet(sheetName);
                sheetRowIndex.put(sheetName, 0);
                if (headerCallback != null) {
                    headerCallback.writeHiddenHeader(workbook, createNextRow());
                    headerCallback.writeHeader(workbook, createNextRow());
                }
            } else {
                sheet = workbook.getSheet(sheetName);
            }

            // TODO very slow, Auto Sizing Columns
//            for (short i = sheet.getRow(0).getFirstCellNum(),
//                 end = sheet.getRow(0).getLastCellNum(); i < end; i++) {
//                sheet.autoSizeColumn(i);
//            }

            Row row = createNextRow();

            for (int i = 0; i < cellValues.length; i++) {
                CellStyle cellStyle = this.cellStyle;
                Object cellValue = cellValues[i];
                Cell cell = null;
                if (cellValue == null) {
                    cell = row.createCell(i, CellType.BLANK);
                } else if (cellValue instanceof String) {
                    cell = row.createCell(i, CellType.STRING);
                    cell.setCellValue(cellValue.toString());
                } else if (cellValue instanceof Integer) {
                    cell = row.createCell(i, CellType.NUMERIC);
                    cell.setCellValue(((Number) cellValue).intValue());
                } else if (cellValue instanceof BigDecimal) {
                    cell = row.createCell(i, CellType.NUMERIC);
                    cell.setCellValue(((Number) cellValue).doubleValue());
                } else if (cellValue instanceof Date) {
                    cell = row.createCell(i);
                    cell.setCellValue((Date) cellValue);
                    cellStyle = this.dateCellStyle;
                } else if (cellValue instanceof DateTime) {
                    cell = row.createCell(i);
                    cell.setCellValue(((DateTime) cellValue).toDate());
                    cellStyle = this.dateCellStyle;
                } else if (cellValue instanceof Calendar) {
                    cell = row.createCell(i, CellType.NUMERIC);
                    cell.setCellValue((Calendar) cellValue);
                } else if (cellValue instanceof Boolean) {
                    cell = row.createCell(i, CellType.BOOLEAN);
                    cell.setCellValue((Boolean) cellValue);
                } else {
                    cell = row.createCell(i, CellType.STRING);
                    cell.setCellValue(cellValue.toString());
                }
                cell.setCellStyle(cellStyle);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (names != null) {
            if (headerCallback == null) {
                headerCallback = new DefaultExcelHeaderCallback();
            }
            if (fieldExtractor == null) {
                BeanWrapperFieldExtractor<T> fieldExtractor = new BeanWrapperFieldExtractor<>();
                fieldExtractor.setNames(names);
                this.fieldExtractor = fieldExtractor;
            }
        }
    }

    private Row createNextRow() {
        currentRowIndex = sheetRowIndex.get(sheetName);
        Row row = sheet.createRow(currentRowIndex);
        currentRowIndex++;
        sheetRowIndex.put(sheetName, currentRowIndex++);
        return row;
    }

    private Font getDefaultFont() {
        Font font = workbook.createFont();
        font.setFontName("Microsoft YaHei");
        return font;
    }

    private CellStyle getDefaultCellStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        return cellStyle;
    }

    class DefaultExcelHeaderCallback implements ExcelHeaderCallback {

        @Override
        public void writeHeader(Workbook workbook, Row row) {
            for (int i = 0; i < names.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(names[i]);
            }
        }

        @Override
        public void writeHiddenHeader(Workbook workbook, Row row) {
        }

        @Override
        public void writeHeaderTips(Workbook workbook, Sheet sheet, Row header) {

        }

        @Override
        public void addDataValidation(Sheet sheet, Row header) {

        }
    }
}
