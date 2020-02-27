package com.actionzh.batch.importer;

import com.actionzh.batch.JobListener;
import com.actionzh.enums.ImportExportTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;

import static com.actionzh.batch.BatchConstants.JOB_PARAM_FILE_NAME;
import static com.actionzh.batch.BatchConstants.JOB_PARAM_TASK_UUID;

/**
 * Created by wangding on 06/07/2017.
 */
public abstract class BaseImportJobListener extends JobListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseImportJobListener.class);


    @Override
    public void beforeJobInternal(JobExecution jobExecution) {
        String uuid = jobExecution.getJobParameters().getString(JOB_PARAM_TASK_UUID);
        String filename = jobExecution.getJobParameters().getString(JOB_PARAM_FILE_NAME);

        //add task record
        jobExecution.getExecutionContext().putLong("app.task.id", 1);
    }

    @Override
    public void afterJobInternal(JobExecution jobExecution) {
        String uuid = jobExecution.getJobParameters().getString(JOB_PARAM_TASK_UUID);
        // task record
    }


    public abstract ImportExportTypeEnum getType();

    public abstract String getNotificationCode();

}
