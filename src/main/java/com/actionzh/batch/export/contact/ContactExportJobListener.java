package com.actionzh.batch.export.contact;

import com.actionzh.batch.BatchConstants;
import com.actionzh.batch.JobListener;
import com.actionzh.batch.MessageSourceHelper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.actionzh.batch.BatchConstants.JOB_PARAM_TASK_UUID;

@Component
@JobScope
public class ContactExportJobListener extends JobListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactExportJobListener.class);

    @Override
    public void beforeJobInternal(JobExecution jobExecution) {
        String uuid = jobExecution.getJobParameters().getString(JOB_PARAM_TASK_UUID);
        String title = MessageSourceHelper.getMessage("batch.export.contact.sheetName");
        String fileName = title + "_" + new DateTime().toString(BatchConstants.FILE_TIMESTAMP_FORMAT) + BatchConstants.FILE_EXT;
        jobExecution.getExecutionContext().putLong("app.task.id", 1);
        try {
            Path temp = Files.createTempFile(title, BatchConstants.FILE_EXT);
            jobExecution.getExecutionContext().putString("temp.file.path", temp.toAbsolutePath().toString());
        } catch (IOException e) {
            LOGGER.error("read file failed", e);
        }
    }

    @Override
    public void afterJobInternal(JobExecution jobExecution) {
        String uuid = jobExecution.getJobParameters().getString(JOB_PARAM_TASK_UUID);
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            String tempFile = jobExecution.getExecutionContext().getString("temp.file.path");
            LOGGER.info("batch COMPLETED path:" + Paths.get(tempFile).toAbsolutePath());
        }
    }
}
