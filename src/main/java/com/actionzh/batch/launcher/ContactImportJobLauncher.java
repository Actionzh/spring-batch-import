package com.actionzh.batch.launcher;

import com.actionzh.utils.AppUuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.actionzh.batch.BatchConstants.*;

/**
 * Created by wangding on 30/06/2017.
 */
@Component
public class ContactImportJobLauncher extends BaseImportLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactImportJobLauncher.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Resource(name = "contactImportJob")
    private Job contactImportJob;

    public String launch(String pathToFile, String filename, String source, String campaign, String medium, String term, String content, Long groupId) throws Exception {
        String uuid = AppUuidGenerator.getNextId();
        try {
            validate(pathToFile);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }

        JobParameters params = new JobParametersBuilder()
                .addString(JOB_PARAM_TASK_UUID, uuid, true)
                .addString(JOB_PARAM_PATH_TO_FILE, pathToFile, true)
                .addString(JOB_PARAM_FILE_NAME, filename, true)
                .addString(JOB_PARAM_SOURCE, source, true)
                .addString(JOB_PARAM_CAMPAIGN, campaign, true)
                .addString(JOB_PARAM_MEDIUM, medium, true)
                .addString(JOB_PARAM_TERM, term, true)
                .addString(JOB_PARAM_CONTENT, content, true)
                .addLong(JOB_PARAM_GROUP_ID, groupId, true)
                .toJobParameters();
        try {
            JobExecution execution = jobLauncher.run(contactImportJob, params);
            LOGGER.info("Exit Status : " + execution.getStatus());
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException ex) {
            throw new Exception(ex.getMessage(), ex);
        }
        return uuid;
    }

}
