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
public class ContactExportJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactExportJobLauncher.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Resource(name = "contactExportJob")
    private Job contactExportJob;

    public String launch(String search, Long groupId, String fields) throws Exception {
        String uuid = AppUuidGenerator.getNextId();
        JobParameters params = new JobParametersBuilder()
                .addString(JOB_PARAM_TASK_UUID, uuid, true)
                .addString(JOB_PARAM_SEARCH, search, true)
                .addLong(JOB_PARAM_GROUP_ID, groupId, true)
                .addString(JOB_PARAM_FIELDS, fields, true)
                .toJobParameters();
        try {
            JobExecution execution = jobLauncher.run(contactExportJob, params);
            LOGGER.info("Exit Status : " + execution.getStatus());
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException ex) {
            throw new Exception(ex.getMessage(), ex);
        }
        return uuid;
    }
}
