package com.actionzh.batch.excel.item;

import org.apache.poi.ss.usermodel.Row;

/**
 * Callback interface for writing a footer to a file.
 *
 * @author Raghavender B
 */
public interface ExcelFooterCallback {

    /**
     * Write contents to a file using the supplied {@link Row}. It is not
     * required to flush the writer inside this method.
     */
    void writeFooter(Row row);
}
