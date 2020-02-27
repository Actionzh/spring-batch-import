package com.actionzh.batch.launcher;

import com.actionzh.utils.AppUuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wangding on 30/06/2017.
 */
@Component
public class ContactExportJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactExportJobLauncher.class);

    @Autowired
    private JobLauncher jobLauncher;

   /* @Resource(name = "contactExportJob")
    private Job contactExportJob;*/

    public String launch(String search, Long groupId, String fields) {
        String uuid = AppUuidGenerator.getNextId();
        /*Long tenantId = TenantContext.getCurrentTenant();
        JobParameters params = new JobParametersBuilder()
                .addLong(JOB_PARAM_TENANT_ID, tenantId, true)
                .addString(JOB_PARAM_TASK_UUID, uuid, true)
                .addString(JOB_PARAM_SEARCH, search, true)
                .addLong(JOB_PARAM_GROUP_ID, groupId, true)
                .addString(JOB_PARAM_FIELDS, fields, true)
                .toJobParameters();
        try {
            JobExecution execution = jobLauncher.run(contactExportJob, params);
            LOGGER.info("Exit Status : " + execution.getStatus());
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException ex) {
            throw new AppException(ExceptionCode.S_00012, ex, ex.getMessage());
        }*/

        return uuid;
    }

}
