package com.actionzh.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public abstract class StepListener implements StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("BEFORE " + this.getClass().getName());
        beforeStepInternal(stepExecution);
    }

    public abstract void beforeStepInternal(StepExecution stepExecution);

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("AFTER " + this.getClass().getName());
        LOGGER.info("reading count [{}]", stepExecution.getReadCount());
        LOGGER.info("writing count [{}]", stepExecution.getWriteCount());
        ExitStatus exitStatus = afterStepInternal(stepExecution);
        return exitStatus == null ? stepExecution.getExitStatus() : exitStatus;
    }

    public abstract ExitStatus afterStepInternal(StepExecution stepExecution);

}
