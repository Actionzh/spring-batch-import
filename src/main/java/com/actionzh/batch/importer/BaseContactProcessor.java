package com.actionzh.batch.importer;

import com.actionzh.batch.MessageSourceHelper;
import com.actionzh.batch.dto.Contact;
import com.actionzh.batch.dto.ContactBatchDTO;
import com.actionzh.exception.InvalidField;
import com.actionzh.exception.InvalidFieldException;
import com.actionzh.utils.TextValidator;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.validator.Validator;
import org.springframework.util.Assert;

import java.util.List;

public abstract class BaseContactProcessor {

    private final Logger LOGGER = LoggerFactory.getLogger(BaseContactProcessor.class);

    private Validator<ContactBatchDTO> validator;


    protected final static List<String> GENDER_VALUES = Lists.newArrayList(Contact.GENDER_TYPE_UNKNOWN, Contact.GENDER_TYPE_FEMALE, Contact.GENDER_TYPE_MALE);

    public abstract void checkInvalidField(ContactBatchDTO contactBatchDTO, List<InvalidField> invalidFields);

    public ContactBatchDTO process(ContactBatchDTO item) {
        LOGGER.debug("Processing contact information: {}", item.toString());
        List<InvalidField> invalidFields = Lists.newArrayList();
        checkInvalidField(item, invalidFields);
        if (item.getDateOfBirthday() != null
                && (item.getDateOfBirthday().isBefore(new DateTime("1900-01-01")) || item.getDateOfBirthday().isAfter(new DateTime("2070-12-31")))) {
            invalidFields.add(InvalidField.builder()
                    .field(MessageSourceHelper.getMessage("batch.export.contact.header.dateOfBirthday"))
                    .value(item.getGender())
                    .invalidError(MessageSourceHelper.getMessage("batch.import.contact.error.date"))
                    .build());
        }
        if (StringUtils.isNotBlank(item.getMobilePhone())
                && !TextValidator.isMobileSimple(item.getMobilePhone())) {
            invalidFields.add(InvalidField.builder()
                    .field(MessageSourceHelper.getMessage("batch.export.contact.header.mobilePhone"))
                    .value(item.getMobilePhone())
                    .invalidError(MessageSourceHelper.getMessage("batch.import.contact.error.mobilePhone"))
                    .build());
        }
        if (StringUtils.isNotBlank(item.getEmail())
                && !TextValidator.isEmail(item.getEmail())) {
            invalidFields.add(InvalidField.builder()
                    .field(MessageSourceHelper.getMessage("batch.export.contact.header.email"))
                    .value(item.getEmail())
                    .invalidError(MessageSourceHelper.getMessage("batch.import.contact.error.email"))
                    .build());
        }
        if (StringUtils.isNotBlank(item.getIdCard()) && !TextValidator.isIdCard(item.getIdCard())) {
            invalidFields.add(InvalidField.builder()
                    .field(MessageSourceHelper.getMessage("batch.export.contact.header.idCard"))
                    .value(item.getIdCard())
                    .invalidError(MessageSourceHelper.getMessage("batch.import.contact.error.idCard"))
                    .build());
        }

        if (invalidFields.size() > 0) {
            throw new InvalidFieldException("Have invalid fields.", invalidFields);
        }
        // this.validator.validate(item);
        return item;
    }

    public void setValidator(Validator<ContactBatchDTO> validator) {
        this.validator = validator;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.validator, "Validator must not be null.");
    }
}
