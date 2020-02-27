package com.actionzh.batch.importer;

import com.actionzh.batch.BatchConstants;
import com.actionzh.batch.StepListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseImportFileStepListener extends StepListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseImportFileStepListener.class);


    @Override
    public void beforeStepInternal(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStepInternal(StepExecution stepExecution) {
        String uuid = stepExecution.getJobParameters().getString(BatchConstants.JOB_PARAM_TASK_UUID);
        if (stepExecution.getSkipCount() == 0) {
            return stepExecution.getExitStatus();
        }
        // 有失败场景，需要导出失败记录
        String pathToImportFile = stepExecution.getJobParameters().getString(BatchConstants.JOB_PARAM_PATH_TO_FILE);
        Path errorFile = createErrorFile(pathToImportFile, uuid);
        //handle error file
        return new ExitStatus("COMPLETED_WITH_ERROR");
    }

    private Path createErrorFile(String pathToImportFile, String uuid) {
        Path errorFile = null;
        try {
            errorFile = Files.createTempFile("contact_error_", BatchConstants.FILE_EXT);
        } catch (IOException e) {
            LOGGER.info("Error to create workbook", e);
        }
        return errorFile;
    }

    protected abstract int getLinesToSkip();
}
