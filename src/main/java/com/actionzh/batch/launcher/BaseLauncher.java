package com.actionzh.batch.launcher;

import com.actionzh.batch.poi.ExcelUtils;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.ss.usermodel.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Author: likai.yu
 * Date: 2019-10-21 15:35
 **/
public abstract class BaseLauncher {

    void validate(String pathToFile) throws Exception {
        Path filePath = Paths.get(pathToFile);
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Workbook workbook = null;
        try {
            fileInputStream = new FileInputStream(pathToFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            boolean isXlsx = FilenameUtils.isExtension(filePath.getFileName().toString(), "xlsx") && DocumentFactoryHelper.hasOOXMLHeader(bufferedInputStream);
            if (!isXlsx) {
                throw new Exception("not xlsx");
            }
            workbook = WorkbookFactory.create(bufferedInputStream);
            if (workbook.getNumberOfSheets() < 1) {
                throw new Exception("sheet number less than 1");
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            boolean emptyRow = ExcelUtils.isRowEmpty(row);
            if (emptyRow) {
                throw new Exception("first row is empty");
            }
            // get first row contents
            List<String> headers = Lists.newArrayList();
            for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                    headers.add(cell.getStringCellValue());
                }
            }
            checkHead(headers);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(bufferedInputStream);
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    public abstract void checkHead(List<String> headers) throws Exception;

}
