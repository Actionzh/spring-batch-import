package com.actionzh.batch.excel.item;
/**
 * @author B Raghavender
 */

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ExcelWorkbookFactory {

    Workbook create(Resource resource) throws IOException;

}
