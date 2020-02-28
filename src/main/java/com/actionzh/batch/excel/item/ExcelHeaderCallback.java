package com.actionzh.batch.excel.item;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Callback interface for writing to a header to a file.
 *
 * @author Raghavender Bandar
 */
public interface ExcelHeaderCallback {

    /**
     * Write contents to a excel file using the supplied {@link Row}. It is not
     * required to flush the writer inside this method.
     */
    void writeHeader(Workbook workbook, Row row);

    void writeHiddenHeader(Workbook workbook, Row row);

    void writeHeaderTips(Workbook workbook, Sheet sheet, Row header);

    void addDataValidation(Sheet sheet, Row header);

}
