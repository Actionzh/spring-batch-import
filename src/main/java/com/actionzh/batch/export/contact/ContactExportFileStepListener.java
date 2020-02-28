package com.actionzh.batch.export.contact;

import com.actionzh.batch.StepListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

/**
 * Created by wangding on 06/07/2017.
 */
@Component
public class ContactExportFileStepListener extends StepListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactExportFileStepListener.class);

    @Override
    public void beforeStepInternal(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStepInternal(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }
}
