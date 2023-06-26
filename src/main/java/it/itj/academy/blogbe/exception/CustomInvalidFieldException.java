package it.itj.academy.blogbe.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomInvalidFieldException extends RuntimeException {
    private final Map<String, String> errors;

    public CustomInvalidFieldException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
