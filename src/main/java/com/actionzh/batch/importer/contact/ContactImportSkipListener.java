package com.actionzh.batch.importer.contact;

import com.actionzh.batch.MessageSourceHelper;
import com.actionzh.batch.dto.ContactBatchDTO;
import com.actionzh.batch.excel.ExcelFileParseException;
import com.actionzh.exception.InvalidField;
import com.actionzh.exception.InvalidFieldException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.List;

/**
 * Created by wangding on 20/07/2017.
 */
@Component
@JobScope
public class ContactImportSkipListener implements SkipListener<ContactBatchDTO, ContactBatchDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactImportSkipListener.class);

  /*  @Autowired
    private ImportExportFailedRecordService importExportFailedRecordService;*/

    @Autowired
    private ContactImportBatchSettings contactImportBatchSettings;

    @Value("#{jobParameters['tenantId']}")
    private Long tenantId;

    @Value("#{jobParameters['uuid']}")
    private String uuid;

    @Override
    public void onSkipInRead(Throwable throwable) {
        StringBuilder error = new StringBuilder();
        error.append(throwable.getMessage() + "\n");
        if (throwable instanceof ExcelFileParseException) {
            ExcelFileParseException ex = (ExcelFileParseException) throwable;
            int rowNumber = ex.getRowNumber();
            LOGGER.info("SkipInRead item num [{}], rowNum [{}] row [{}]", ex.getRowNumber(), rowNumber, StringUtils.join(ex.getRow(), ","));
            if (ex.getCause() instanceof BindException) {
                String filed = ((BindException) ex.getCause()).getFieldErrors().get(0).getField();
                filed = MessageSourceHelper.getMessage("batch.export.contact.header." + filed);
                error.append("invalid field: " + filed + "\n");
            }
            writeFailedRecord(rowNumber, StringUtils.abbreviate(error.toString(), 500));
        }
        LOGGER.info("SkipInRead item reason [{}]", error.toString());
    }

    private String getError(Throwable throwable) {
        StringBuilder error = new StringBuilder();
        error.append(throwable.getMessage() + "\n");
        if (throwable instanceof InvalidFieldException) {
            InvalidFieldException ex = (InvalidFieldException) throwable;
            List<InvalidField> fields = ex.getInvalidFields();
            for (InvalidField filed : fields) {
                error.append(filed.getInvalidError() + ": " + filed.getField() + "\n");
            }
        }
        return StringUtils.abbreviate(error.toString(), 500);
    }

    @Override
    public void onSkipInWrite(ContactBatchDTO contactBatchDTO, Throwable throwable) {
        int rowIndex = contactBatchDTO.getCurrentItemCount() + contactImportBatchSettings.getLinesToSkip() - 1;
        LOGGER.info("SkipInWrite item name [{}] rowNum [{}], reason [{}]", contactBatchDTO.getName(), rowIndex, throwable.getMessage());
        writeFailedRecord(rowIndex, getError(throwable));
    }

    @Override
    public void onSkipInProcess(ContactBatchDTO contactBatchDTO, Throwable throwable) {
        int rowIndex = contactBatchDTO.getCurrentItemCount() + contactImportBatchSettings.getLinesToSkip() - 1;
        LOGGER.info("SkipInProcess item name [{}] rowNum [{}], reason [{}]", contactBatchDTO.getName(), rowIndex, throwable.getMessage());
        writeFailedRecord(rowIndex, getError(throwable));
    }

    private void writeFailedRecord(Integer rowNum, String msg) {
        /*ImportExportFailedRecordDTO recordDTO = ImportExportFailedRecordDTO.builder()
                .uuid(uuid)
                .excelRowNum(rowNum)
                .errorMessage(msg)
                .build();
        importExportFailedRecordService.create(recordDTO);*/
    }

}
