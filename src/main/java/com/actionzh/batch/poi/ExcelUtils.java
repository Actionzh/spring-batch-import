package com.actionzh.batch.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtils {
    /**
     * Copies a row from a row index on the given workbook and sheet to another row index. If the destination row is
     * already occupied, shift all rows down to make room.
     */
    public static void copyRow(Workbook workbook, Sheet worksheet, int from, int to) {
        Row sourceRow = worksheet.getRow(from);
        Row newRow = worksheet.getRow(to);

        if (alreadyExists(newRow))
            worksheet.shiftRows(to, worksheet.getLastRowNum(), 1);
        else
            newRow = worksheet.createRow(to);

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);
            if (oldCell != null) {
                copyCellStyle(oldCell, newCell);
                copyCellComment(oldCell, newCell);
                copyCellHyperlink(oldCell, newCell);
                copyCellDataTypeAndValue(oldCell, newCell);
            }
        }

        copyAnyMergedRegions(worksheet, sourceRow, newRow);
    }

    public static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }

    private static void copyCellStyle(Cell oldCell, Cell newCell) {
        newCell.setCellStyle(oldCell.getCellStyle());
    }

    private static void copyCellComment(Cell oldCell, Cell newCell) {
        if (newCell.getCellComment() != null)
            newCell.setCellComment(oldCell.getCellComment());
    }

    private static void copyCellHyperlink(Cell oldCell, Cell newCell) {
        if (oldCell.getHyperlink() != null)
            newCell.setHyperlink(oldCell.getHyperlink());
    }

    private static void copyCellDataTypeAndValue(Cell oldCell, Cell newCell) {
        setCellDataType(oldCell, newCell);
        setCellDataValue(oldCell, newCell);
    }

    private static void setCellDataType(Cell oldCell, Cell newCell) {
        newCell.setCellType(oldCell.getCellTypeEnum());
    }

    private static void setCellDataValue(Cell oldCell, Cell newCell) {
        switch (oldCell.getCellTypeEnum()) {
            case BLANK:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            case NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case STRING:
                newCell.setCellValue(oldCell.getRichStringCellValue());
                break;
        }
    }

    private static boolean alreadyExists(Row newRow) {
        return newRow != null;
    }

    private static void copyAnyMergedRegions(Sheet worksheet, Row sourceRow, Row newRow) {
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++)
            copyMergeRegion(worksheet, sourceRow, newRow, worksheet.getMergedRegion(i));
    }

    private static void copyMergeRegion(Sheet worksheet, Row sourceRow, Row newRow, CellRangeAddress mergedRegion) {
        CellRangeAddress range = mergedRegion;
        if (range.getFirstRow() == sourceRow.getRowNum()) {
            int lastRow = newRow.getRowNum() + (range.getFirstRow() - range.getLastRow());
            CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(), lastRow, range.getFirstColumn(), range.getLastColumn());
            worksheet.addMergedRegion(newCellRangeAddress);
        }
    }

    public static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK)
                return false;
        }
        return true;
    }

    public static void addComment(Workbook workbook, Sheet sheet, Row row, Cell cell, String message, int width, int height) {
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        // When the comment box is visible, have it show in a 1x5 space
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + width);
        anchor.setRow1(row.getRowNum());
        anchor.setRow2(row.getRowNum() + height);
        // Create the comment and set the text+author
        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(message);
        comment.setString(str);
        // Assign the comment to the cell
        cell.setCellComment(comment);
    }
}
