package it.itj.academy.blogbe.exception;

import java.util.Map;

public class CustomInvalidValidationFiledConstraintException extends RuntimeException {
    private final Map<String, String> errors;

    public CustomInvalidValidationFiledConstraintException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
