package com.actionzh.enums;


/**
 * Created by wangding on 16/04/2017.
 */
public enum ImportExportStatusEnum implements StringBaseEnum {

    STARTED("STARTED"),
    COMPLETED("COMPLETED"),
    COMPLETED_WITH_ERROR("COMPLETED_WITH_ERROR"),
    FAILED("FAILED");

    private String type;

    private ImportExportStatusEnum(String type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        return this.type;
    }

}
