package com.actionzh.batch.importer.contact;

import com.actionzh.batch.importer.BaseImportJobListener;
import com.actionzh.enums.ImportExportTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class ContactImportJobListener extends BaseImportJobListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactImportJobListener.class);


    @Override
    public ImportExportTypeEnum getType() {
        return ImportExportTypeEnum.IMPORT;
    }

    @Override
    public String getNotificationCode() {
        return "IMPORT";
    }
}
