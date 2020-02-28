package com.actionzh.batch.export.contact;

import com.actionzh.batch.BatchSettings;
import com.actionzh.batch.MessageSourceHelper;
import com.actionzh.batch.excel.item.ExcelHeaderCallback;
import com.actionzh.batch.poi.ExcelUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Created by wangding on 04/07/2017.
 */
public class ContactExportHeaderCallback implements ExcelHeaderCallback {


    private final BatchSettings batchSettings;

    private String[] cols;

    private String[] titles;

    private Font font;

    private static final String[] GENDERS = {"male", "female", "unknown"};

    public ContactExportHeaderCallback(BatchSettings batchSettings) {
        this.batchSettings = batchSettings;
        init();
    }

    private void init() {
        String[] titles = new String[batchSettings.getColumns().length];
        for (int i = 0; i < batchSettings.getColumns().length; i++) {
            titles[i] = MessageSourceHelper.getMessage("batch.export.contact.header." + batchSettings.getColumns()[i]);
        }
        this.titles = ArrayUtils.addAll(titles);
        this.cols = ArrayUtils.addAll(batchSettings.getColumns());
    }

    @Override
    public void writeHeader(Workbook workbook, Row row) {
        CellStyle cellStyle = getDefaultCellStyle(workbook);
        write(titles, row, cellStyle);
    }

    @Override
    public void writeHiddenHeader(Workbook workbook, Row row) {
        CellStyle cellStyle = workbook.createCellStyle();
        write(cols, row, cellStyle);
        row.setZeroHeight(true);
    }

    private void write(String[] data, Row row, CellStyle cellStyle) {
        if (data.length != 0) {
            for (int i = 0; i < data.length; i++) {
                Cell cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(data[i]);
                cell.setCellStyle(cellStyle);
            }
        }
    }

    @Override
    public void writeHeaderTips(Workbook workbook, Sheet sheet, Row header) {
        String message = null;
        for (int i = 0; i < cols.length; i++) {
            Cell cell = header.getCell(i);
            switch (cols[i]) {
                case "dateOfBirthday":
                    message = MessageSourceHelper.getMessage("batch.import.contact.header.date.tips");
                    ExcelUtils.addComment(workbook, sheet, header, cell, message, 4, 2);
                    break;
                case "mobilePhone":
                case "homePhone":
                case "postalCode":
                    break;
                case "gender":
                    message = MessageSourceHelper.getMessage("batch.import.contact.header.gender.tips");
                    ExcelUtils.addComment(workbook, sheet, header, cell, message, 4, 2);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void addDataValidation(Sheet sheet, Row header) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        for (int i = 0; i < cols.length; i++) {
            switch (cols[i]) {
                case "gender":
                    XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                            .createExplicitListConstraint(GENDERS);
                    CellRangeAddressList addressList = new CellRangeAddressList(2, 65535, i, i);
                    XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
                    validation.setSuppressDropDownArrow(true);
                    validation.setShowErrorBox(true);
                    sheet.addValidationData(validation);
                    break;
                default:
                    break;
            }
        }


    }

    private Font getDefaultFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Microsoft YaHei");
        font.setBold(true);
        return font;
    }

    private CellStyle getDefaultCellStyle(Workbook workbook) {
        if (font == null) {
            font = getDefaultFont(workbook);
        }
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        return cellStyle;
    }
}
