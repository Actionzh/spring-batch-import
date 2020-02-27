package com.actionzh.batch.importer.contact;

import com.actionzh.batch.importer.BaseImportFileStepListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wangding on 06/07/2017.
 */
@Component
public class ContactImportFileStepListener extends BaseImportFileStepListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactImportFileStepListener.class);

    @Autowired
    private ContactImportBatchSettings contactImportBatchSettings;

    @Override
    protected int getLinesToSkip() {
        return contactImportBatchSettings.getLinesToSkip();
    }
}
