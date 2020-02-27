package com.actionzh.enums;

/**
 * Created by wangding on 16/04/2017.
 */
public enum ImportExportTypeEnum implements StringBaseEnum {

    IMPORT("IMPORT"),
    UPDATE("UPDATE"),
    EXPORT("EXPORT");

    private String type;

    private ImportExportTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        return this.type;
    }

}
