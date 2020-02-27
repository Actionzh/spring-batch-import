package com.actionzh.exception;


import java.util.ArrayList;
import java.util.List;

public class InvalidFieldException extends RuntimeException {

    private final List<InvalidField> invalidFields;

    public List<InvalidField> getInvalidFields() {
        return invalidFields;
    }

    public InvalidFieldException(String message, List<InvalidField> invalidFields) {
        super(message);
        this.invalidFields = invalidFields;
    }

    public InvalidFieldException(Throwable cause, List<InvalidField> invalidFields) {
        super(cause);
        this.invalidFields = invalidFields;
    }

    public InvalidFieldException(Throwable cause, String fieldName, String fieldValue) {
        super(cause);
        this.invalidFields = new ArrayList<>();
        this.invalidFields.add(InvalidField.builder().invalidError(cause.getMessage()).field(fieldName).value(fieldValue).build());
    }

}
