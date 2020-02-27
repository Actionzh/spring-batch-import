package com.actionzh.service.impl;

import com.actionzh.batch.dto.Contact;
import com.actionzh.batch.dto.ContactDTO;
import com.actionzh.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fy on 04/05/2017.
 */
@Service("contactService")
public class ContactServiceImpl implements ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public ContactDTO create(ContactDTO contactDTO) {
        Contact entity = convert(contactDTO);
        entity = contactRepository.saveAndFlush(entity);
        return contactDTO;
    }

    private Contact convert(ContactDTO contactDTO) {
        Contact entity = new Contact();
        entity.setName(contactDTO.getName());
        return entity;

    }
}
