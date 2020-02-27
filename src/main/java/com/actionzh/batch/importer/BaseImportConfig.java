package com.actionzh.batch.importer;

import com.actionzh.batch.excel.rowset.DefaultRowSetFactory;
import com.actionzh.batch.excel.rowset.RowNumberColumnNameExtractor;
import com.actionzh.batch.excel.rowset.RowSetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseImportConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseImportConfig.class);

    protected RowSetFactory rowSetFactory() {
        DefaultRowSetFactory rowSetFactory = new DefaultRowSetFactory();
        RowNumberColumnNameExtractor columnNameExtractor = new RowNumberColumnNameExtractor();
        columnNameExtractor.setHeaderRowNumber(0);
        rowSetFactory.setColumnNameExtractor(columnNameExtractor);
        return rowSetFactory;
    }

    protected Tasklet getDeleteFileTask(String pathToFile) {
        return (final StepContribution contribution, final ChunkContext chunkContext) -> {
            final Path file = Paths.get(pathToFile);
            LOGGER.info("Deleting file [{}]", pathToFile);
            Files.deleteIfExists(file);
            return RepeatStatus.FINISHED;
        };
    }
}
