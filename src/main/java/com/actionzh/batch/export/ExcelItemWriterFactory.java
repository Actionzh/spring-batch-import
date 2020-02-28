package com.actionzh.batch.export;

import com.actionzh.batch.BatchSettings;
import com.actionzh.batch.MessageSourceHelper;
import com.actionzh.batch.dto.ContactBatchDTO;
import com.actionzh.batch.excel.item.ExcelItemWriter;
import com.actionzh.batch.export.contact.ContactExportBatchSettings;
import com.actionzh.batch.export.contact.ContactExportHeaderCallback;
import com.actionzh.batch.format.UDFBeanWrapperFieldExtractor;
import com.actionzh.utils.JsonMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangding on 21/07/2017.
 */
@Component
public class ExcelItemWriterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelItemWriterFactory.class);

    protected final static JsonMapper jsonMapper = JsonMapper.INSTANCE;

    public ExcelItemWriter<?> newInstance(Long tenantId, String pathToFile, BatchSettings batchSettings, String fields) {
        ExcelItemWriter<?> excelItemWriter = null;
        if (batchSettings instanceof ContactExportBatchSettings) {
            excelItemWriter = newContactExcelWriter(tenantId, pathToFile, batchSettings, fields);
        }
        return excelItemWriter;
    }

    private ExcelItemWriter<ContactBatchDTO> newContactExcelWriter(Long tenantId, String pathToFile, BatchSettings batchSettings, String fields) {
        ExcelItemWriter<ContactBatchDTO> itemWriter = new ExcelItemWriter<>();
        String title = MessageSourceHelper.getMessage("batch.export.contact.sheetName");
        LOGGER.info("Contact Export File [{}]", pathToFile);
        itemWriter.setResource(new FileSystemResource(pathToFile));
        itemWriter.setName(title);
        itemWriter.setSheetName(title);
        String[] defaultColumns = batchSettings.getColumns();
        if (!StringUtils.isEmpty(fields)) {
            List<String> fieldList = jsonMapper.fromJson(fields, jsonMapper.buildCollectionType(List.class, String.class));
            List<String> fieldListConvert = new ArrayList<>();
            List<String> metaList = new ArrayList<>();
            for (String str : fieldList) {
                if (str.startsWith("attr")) {
                    metaList.add(str);
                    continue;
                }
                fieldListConvert.add(str);
            }
            String[] cloumns = new String[fieldListConvert.size()];
            batchSettings.setColumns(fieldListConvert.toArray(cloumns));
        }
        itemWriter.setHeaderCallback(new ContactExportHeaderCallback(batchSettings));
        UDFBeanWrapperFieldExtractor<ContactBatchDTO> extractor = new UDFBeanWrapperFieldExtractor<>();
        String[] names = ArrayUtils.addAll(batchSettings.getColumns());
        extractor.setNames(names);
        LOGGER.info("Contact Export Extractor names [{}]", StringUtils.join(names, ","));
        itemWriter.setFieldExtractor(extractor);
        batchSettings.setColumns(defaultColumns);
        return itemWriter;
    }
}
