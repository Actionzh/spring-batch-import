package com.actionzh.batch.excel.item;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author B Raghavender
 */
public class DefaultExcelWorkbookFactory implements ExcelWorkbookFactory {

    @Override
    public Workbook create(Resource resource) throws IOException {
        String ext = getFileExtension(resource);
        Workbook wb = null;
        if ("XLS".equals(ext)) {
            wb = new HSSFWorkbook();
        } else if ("XLSX".equals(ext)) {
            wb = new SXSSFWorkbook(2000);
        } else {
            throw new UnknownFileTypeException("Support only *.xls and *.xlsx");
        }
        return wb;
    }

    private String getFileExtension(Resource resource) {
        String ext = FilenameUtils.getExtension(resource.getFilename());
        ext = ext.toUpperCase();
        return ext;
    }


}
