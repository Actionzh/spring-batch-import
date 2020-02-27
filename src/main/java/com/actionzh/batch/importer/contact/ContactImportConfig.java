package com.actionzh.batch.importer.contact;

import com.actionzh.batch.dto.ContactBatchDTO;
import com.actionzh.batch.excel.mapping.BeanWrapperRowMapper;
import com.actionzh.batch.excel.poi.PoiItemReader;
import com.actionzh.batch.importer.BaseImportConfig;
import com.actionzh.batch.importer.BatchBeanValidator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

import java.beans.PropertyEditor;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(value = "com.actionzh.config")
public class ContactImportConfig extends BaseImportConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactImportConfig.class);

    @Value("#{appConfig.batchInitialBackOffInterval}")
    private int initialBackOffInterval;

    @Value("#{appConfig.batchMaxBackOffInterval}")
    private int maxBackOffInterval;

    @Value("#{appConfig.batchRetryLimit}")
    private int retryLimit;

    @Value("#{appConfig.batchSkipLimit}")
    private int skipLimit;

    @Bean
    @JobScope
    PoiItemReader<ContactBatchDTO> contactExcelReader(ContactImportBatchSettings contactImportBatchSettings,
                                                      @Value("#{jobParameters['pathToFile']}") String pathToFile) {
        PoiItemReader<ContactBatchDTO> reader = new PoiItemReader<>();
        reader.setLinesToSkip(contactImportBatchSettings.getLinesToSkip());
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(pathToFile); // NOSONAR
            reader.setResource(new InputStreamResource(new BufferedInputStream(inputStream)));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
        BeanWrapperRowMapper<ContactBatchDTO> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(ContactBatchDTO.class);
        rowMapper.setCustomEditors(createCustomEditors());
        reader.setRowMapper(rowMapper);
        reader.setRowSetFactory(rowSetFactory());
        return reader;
    }

    private Map<?, ? extends PropertyEditor> createCustomEditors() {
        Map<Object, PropertyEditor> editorHashMap = new HashMap<>();
        editorHashMap.put(DateTime.class, new JodaDateTimeEditor());
        return editorHashMap;
    }

    @Bean
    @JobScope
    public Tasklet deleteFileTasklet(@Value("#{jobParameters['pathToFile']}") String pathToFile) {
        return getDeleteFileTask(pathToFile);
    }

    @Bean
    public Step deleteFileStep(@Qualifier("deleteFileTasklet") final Tasklet deleteFileTasklet,
                               final StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory
                .get("deleteFileStep")
                .tasklet(deleteFileTasklet)
                .build();
    }

    @Bean
    ItemProcessor<ContactBatchDTO, ContactBatchDTO> importContactProcessor() {
        ImportContactProcessor processor = new ImportContactProcessor();
        processor.setValidator(new BatchBeanValidator<>());
        return processor;
    }

    @Bean
    @JobScope
    Step contactImportStep(ItemReader<ContactBatchDTO> contactExcelReader,
                           @Value("#{jobParameters['tenantId']}") Long tenantId,
                           @Value("#{jobParameters['uuid']}") String uuid,
                           ContactImportBatchSettings contactImportBatchSettings,
                           @Qualifier(value = "importContactProcessor") ItemProcessor<ContactBatchDTO, ContactBatchDTO> importContactProcessor,
                           ContactWriter contactWriter,
                           ContactImportFileStepListener contactImportFileStepListener,
                           ContactImportSkipListener contactImportSkipListener,
                           StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("contactImportStep")
                .<ContactBatchDTO, ContactBatchDTO>chunk(contactImportBatchSettings.getWriterChunkSize())
                .reader(contactExcelReader)
                .processor(importContactProcessor)
                .writer(contactWriter)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(contactImportSkipListener)
                .retryLimit(retryLimit)
                .retry(JpaOptimisticLockingFailureException.class)
                .backOffPolicy(backOffPolicy())
                .listener(contactImportFileStepListener)
                .build();
    }

    @Bean
    Job contactImportJob(JobBuilderFactory jobBuilderFactory,
                         final Step deleteFileStep,
                         ContactImportJobListener contactImportJobListener,
                         @Qualifier("contactImportStep") Step contactImportStep) {
        return jobBuilderFactory.get("contactImportJob")
                .incrementer(new RunIdIncrementer())
                .start(contactImportStep)
                .next(deleteFileStep)
                .listener(contactImportJobListener)
                .build();
    }

    private BackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initialBackOffInterval);
        backOffPolicy.setMaxInterval(maxBackOffInterval);
        return backOffPolicy;
    }

    @Bean
    @ConfigurationProperties(prefix = "batch.import.contact")
    ContactImportBatchSettings contactImportBatchSettings() {
        return new ContactImportBatchSettings();
    }

}
