package com.actionzh.batch.importer.contact;

import org.joda.time.DateTime;

import java.beans.PropertyEditorSupport;

/**
 * Created by wangding on 04/07/2017.
 */
public class JodaDateTimeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        if (text != null && !"".equals(text)) {
            DateTime dt;
            try {
                dt = new DateTime(text);
            } catch (IllegalArgumentException ex) {
                // maybe a timestamp
                dt = new DateTime(Long.valueOf(text));
            }
            setValue(dt); // date time in ISO8601 format
        }
        // (yyyy-MM-ddTHH:mm:ss.SSSZZ)
    }

    @Override
    public void setValue(final Object value) {
        super.setValue(value == null || value instanceof DateTime ? value
                : new DateTime(value));
    }

    @Override
    public DateTime getValue() {
        return (DateTime) super.getValue();
    }

    @Override
    public String getAsText() {
        return getValue().toString(); // date time in ISO8601 format
        // (yyyy-MM-ddTHH:mm:ss.SSSZZ)
    }
}
