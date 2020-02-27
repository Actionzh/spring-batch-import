package com.actionzh.batch.importer;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BatchBeanValidator<T> implements Validator<T>, InitializingBean {

    private javax.validation.Validator validator;


    @Override
    public void validate(T value) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(value);

        if (constraintViolations.size() > 0) {
            buildValidationException(constraintViolations);
        }
    }

    private void buildValidationException(
            Set<ConstraintViolation<Object>> constraintViolations) {
        StringBuilder message = new StringBuilder();

        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            message.append(constraintViolation.getMessage() + "\n");
        }

        throw new ValidationException(message.toString());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
}
