package com.actionzh.batch.launcher;

import com.actionzh.utils.AppUuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: likai.yu
 * Date: 2019-10-17 09:44
 **/
@Component
public class ContactUpdateJobLauncher extends BaseUpdateLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUpdateJobLauncher.class);

    @Autowired
    private JobLauncher jobLauncher;

   /* @Resource(name = "contactUpdateJob")
    private Job contactUpdateJob;*/


    public String launch(String pathToFile, String filename) {
        String uuid = AppUuidGenerator.getNextId();
        /*try {
            validate(pathToFile);
        } catch (AppException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BizException(ExceptionCode.B_00016, ex, ex.getMessage());
        }
        Long tenantId = resolver.getCurrentTenant();

        JobParameters params = new JobParametersBuilder()
                .addLong(JOB_PARAM_TENANT_ID, tenantId, true)
                .addString(JOB_PARAM_TASK_UUID, uuid, true)
                .addString(JOB_PARAM_PATH_TO_FILE, pathToFile, true)
                .addString(JOB_PARAM_FILE_NAME, filename, true)
                .toJobParameters();
        try {
            JobExecution execution = jobLauncher.run(contactUpdateJob, params);
            LOGGER.info("Exit Status : " + execution.getStatus());
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException ex) {
            throw new AppException(ExceptionCode.S_00012, ex, ex.getMessage());
        }*/
        return uuid;
    }
}
