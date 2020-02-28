package com.actionzh.batch.format;

import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangding on 04/07/2017.
 */
public class UDFBeanWrapperFieldExtractor<T> extends BeanWrapperFieldExtractor<T> {

    private String[] names;


    public UDFBeanWrapperFieldExtractor() {
    }


    @Override
    public void setNames(String[] names) {
        Assert.notNull(names, "Names must be non-null");
        this.names = Arrays.asList(names).toArray(new String[names.length]);
    }

    @Override
    public Object[] extract(T item) {

       /* Map<String, ContactMetaPropertyDTO> fieldMetaMap = new HashMap<>();
        for (ContactMetaPropertyDTO meta : fieldMeta) {
            fieldMetaMap.put(meta.getDbColumnName(), meta);
        }*/

        List<Object> values = new ArrayList<>();
        BeanWrapper bw = new BeanWrapperImpl(item);
        String[] arr$ = this.names;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String propertyName = arr$[i$];
            Object value = bw.getPropertyValue(propertyName);
            // UDF
           /* String pattern = "^attr([1-9]|[1-6][0-4])$";
            if (value != null && Pattern.matches(pattern, propertyName)) {
                ContactMetaPropertyDTO meta = fieldMetaMap.get(propertyName);
                if (meta.getDataType().equals(MetaPropDataTypes.INTEGER)) {
                    value = Integer.parseInt(value.toString());
                } else if (meta.getDataType().equals(MetaPropDataTypes.BOOLEAN)) {
                    value = Boolean.valueOf(value.toString());
                } else if (meta.getDataType().equals(MetaPropDataTypes.DATETIME)) {
                    // 2017-04-08 16:56:55
                    value = DateTimeUtil.parseDateTime(value.toString());
                }
            }*/
            values.add(value);
        }
        return values.toArray();
    }

}
