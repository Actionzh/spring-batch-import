package com.actionzh.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvalidField {

    private String invalidError;
    private String field;
    private String value;

}
