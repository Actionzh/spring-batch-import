package com.actionzh.batch.importer.contact;

import com.actionzh.batch.dto.ContactBatchDTO;
import com.actionzh.batch.dto.ContactDTO;
import com.actionzh.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@JobScope
public class ContactWriter implements ItemWriter<ContactBatchDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactWriter.class);
    @Autowired
    private ContactService contactService;


    @Value("#{jobParameters['source']}")
    private String source;

    @Value("#{jobParameters['campaign']}")
    private String campaign;

    @Value("#{jobParameters['medium']}")
    private String medium;

    @Value("#{jobParameters['term']}")
    private String term;

    @Value("#{jobParameters['content']}")
    private String content;

    @Value("#{jobParameters['groupId']}")
    private Long groupId;

    @PostConstruct
    public void init() {
    }

    @Override
    public void write(List<? extends ContactBatchDTO> items) throws Exception {
        LOGGER.info("Writing to database with " + items.size() + " items.");
        for (final ContactBatchDTO item : items) {
            LOGGER.info("Create contact name [{}]", item.getName());
            ContactDTO contactDTO = contactService.create(item);
        }
    }

}
