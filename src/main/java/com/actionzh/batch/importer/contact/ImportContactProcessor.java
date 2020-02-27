package com.actionzh.batch.importer.contact;

import com.actionzh.batch.MessageSourceHelper;
import com.actionzh.batch.dto.Contact;
import com.actionzh.batch.dto.ContactBatchDTO;
import com.actionzh.batch.importer.BaseContactProcessor;
import com.actionzh.exception.InvalidField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class ImportContactProcessor extends BaseContactProcessor implements ItemProcessor<ContactBatchDTO, ContactBatchDTO>, InitializingBean {


    @Override
    public void checkInvalidField(ContactBatchDTO contactBatchDTO, List<InvalidField> invalidFields) {
        //invalidFields is not null
        if (StringUtils.isBlank(contactBatchDTO.getName())) {
            invalidFields.add(InvalidField.builder()
                    .field(MessageSourceHelper.getMessage("batch.export.contact.header.name"))
                    .value(contactBatchDTO.getName())
                    .invalidError(MessageSourceHelper.getMessage("batch.import.contact.error.name"))
                    .build());
        }
        if (StringUtils.isBlank(contactBatchDTO.getGender())) {
            contactBatchDTO.setGender(Contact.GENDER_TYPE_UNKNOWN);
        }
        if (!GENDER_VALUES.contains(contactBatchDTO.getGender())) {
            invalidFields.add(InvalidField.builder()
                    .field(MessageSourceHelper.getMessage("batch.export.contact.header.gender"))
                    .value(contactBatchDTO.getGender())
                    .invalidError(MessageSourceHelper.getMessage("batch.import.contact.error.gender"))
                    .build());
        }
    }

}
