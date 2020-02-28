package com.actionzh.batch.export.contact;

import com.actionzh.batch.importer.contact.JodaDateTimeEditor;
import org.joda.time.DateTime;
import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * Created by wangding on 04/07/2017.
 */
public class CustomBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {

    public CustomBeanPropertyRowMapper(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    @Override
    protected void initBeanWrapper(BeanWrapper bw) {
        bw.registerCustomEditor(DateTime.class, new JodaDateTimeEditor());
    }
}