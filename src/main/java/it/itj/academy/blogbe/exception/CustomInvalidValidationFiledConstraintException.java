package it.itj.academy.blogbe.exception;

import java.util.List;
import java.util.Map;

public class CustomInvalidValidationFiledConstraintException extends RuntimeException {
    private final Map<String, List<String>> errors;

    public CustomInvalidValidationFiledConstraintException(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
