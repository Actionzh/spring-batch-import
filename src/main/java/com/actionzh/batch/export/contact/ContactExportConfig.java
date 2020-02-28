package com.actionzh.batch.export.contact;

import com.actionzh.batch.dto.ContactBatchDTO;
import com.actionzh.batch.excel.item.ExcelItemWriter;
import com.actionzh.batch.export.ExcelItemWriterFactory;
import com.actionzh.config.AppConfig;
import com.actionzh.service.impl.ContactRepository;
import com.actionzh.service.impl.SqlQueryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangding on 30/06/2017.
 */
@Configuration
public class ContactExportConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactExportConfig.class);

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ExcelItemWriterFactory excelItemWriterFactory;

    @Autowired
    private AppConfig appConfig;

    @Bean
    @JobScope
    ItemReader<ContactBatchDTO> contactDatabaseReader(DataSource dataSource,
                                                      ContactExportBatchSettings contactExportBatchSettings,
                                                      @Value("#{jobParameters['search']}") String search,
                                                      @Value("#{jobParameters['groupId']}") Long groupId) {
        JdbcPagingItemReader<ContactBatchDTO> databaseReader = new JdbcPagingItemReader<>();

        databaseReader.setDataSource(dataSource);
        databaseReader.setPageSize(contactExportBatchSettings.getReaderPageSize());
        databaseReader.setMaxItemCount(appConfig.getExportMaxRow());
        databaseReader.setRowMapper(new CustomBeanPropertyRowMapper<>(ContactBatchDTO.class));

        SqlQueryProvider sqlQueryProvider = contactRepository.getQueryProvider(null, groupId, search, 0);
        PagingQueryProvider queryProvider = create(sqlQueryProvider);
        databaseReader.setQueryProvider(queryProvider);

        databaseReader.setParameterValues(sqlQueryProvider.getParameterValues());
        return databaseReader;
    }

    private MySqlPagingQueryProvider create(SqlQueryProvider sqlQueryProvider) {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(sqlQueryProvider.getSelectClause());
        queryProvider.setFromClause(sqlQueryProvider.getFromClause());
        queryProvider.setWhereClause(sqlQueryProvider.getWhereClause());
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("c.id", Order.ASCENDING);
        queryProvider.setSortKeys(sortConfiguration);
        return queryProvider;
    }

    @Bean
    ItemProcessor<ContactBatchDTO, ContactBatchDTO> exportContactProcessor() {
        return new ExportContactProcessor();
    }

    @Bean
    @JobScope
    ExcelItemWriter<ContactBatchDTO> contactExcelItemWriter(ContactExportBatchSettings contactExportBatchSettings,
                                                            @Value("#{jobExecutionContext['temp.file.path']}") String tempFile,
                                                            @Value("#{jobParameters['tenantId']}") Long tenantId,
                                                            @Value("#{jobParameters['fields']}") String fields) {
        return (ExcelItemWriter<ContactBatchDTO>) excelItemWriterFactory.newInstance(tenantId, tempFile, contactExportBatchSettings, fields);
    }

    @Bean
    Step contactExportStep(@Qualifier("contactDatabaseReader") ItemReader<ContactBatchDTO> contactDatabaseReader,
                           ContactExportBatchSettings contactExportBatchSettings,
                           @Qualifier("exportContactProcessor") ItemProcessor<ContactBatchDTO, ContactBatchDTO> exportContactProcessor,
                           @Qualifier("contactExcelItemWriter") ExcelItemWriter<ContactBatchDTO> contactExcelItemWriter,
                           ContactExportFileStepListener contactExportFileStepListener,
                           StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("contactExportStep1")
                .<ContactBatchDTO, ContactBatchDTO>chunk(contactExportBatchSettings.getWriterChunkSize())
                .reader(contactDatabaseReader)
                .processor(exportContactProcessor)
                .writer(contactExcelItemWriter)
                .listener(contactExportFileStepListener)
                .build();
    }

    @Bean
    Job contactExportJob(JobBuilderFactory jobBuilderFactory,
                         ContactExportJobListener contactExportJobListener,
                         @Qualifier("contactExportStep") Step contactExportStep) {
        return jobBuilderFactory.get("contactExportJob")
                .incrementer(new RunIdIncrementer())
                .flow(contactExportStep)
                .end()
                .listener(contactExportJobListener)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "batch.export.contact")
    ContactExportBatchSettings contactExportBatchSettings() {
        return new ContactExportBatchSettings();
    }

    public class ExportContactProcessor implements ItemProcessor<ContactBatchDTO, ContactBatchDTO> {

        private final Logger LOGGER = LoggerFactory.getLogger(ExportContactProcessor.class);

        @Override
        public ContactBatchDTO process(ContactBatchDTO item) throws Exception {
            // TODO need change to debug later
            LOGGER.info("Processing contact information: {}", item.toString());
            return item;
        }
    }
}
