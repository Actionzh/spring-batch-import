package com.actionzh.batch.importer;

import com.actionzh.batch.BatchConstants;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class TemplateGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateGenerator.class);

    public Path generate(ExcelHeaderCallback headerCallback, String sheetName) {
        Path tempFile = null;
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);
            headerCallback.writeHiddenHeader(workbook, sheet.createRow(0));
            Row header = sheet.createRow(1);
            headerCallback.writeHeader(workbook, header);
            headerCallback.writeHeaderTips(workbook, sheet, header);
            headerCallback.addDataValidation(sheet, header);
            tempFile = Files.createTempFile("template_", BatchConstants.FILE_EXT);
            OutputStream outputStream = Files.newOutputStream(tempFile);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        if (tempFile != null) {
            LOGGER.info("Generate template at [{}]", tempFile.toAbsolutePath());
        }
        return tempFile;
    }

}
