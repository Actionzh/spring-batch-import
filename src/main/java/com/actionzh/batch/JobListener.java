package com.actionzh.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public abstract class JobListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobListener.class);


    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("BEFORE " + this.getClass().getName());
        beforeJobInternal(jobExecution);
    }

    public abstract void beforeJobInternal(JobExecution jobExecution);

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("AFTER " + this.getClass().getName());
        try {
            afterJobInternal(jobExecution);
        } finally {
        }
    }

    public abstract void afterJobInternal(JobExecution jobExecution);

}
