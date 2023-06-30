package it.itj.academy.blogbe.config;

import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.exception.CustomInvalidValidationFiledConstraintException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = CustomInvalidValidationFiledConstraintException.class)
    public ResponseEntity<Map<String, List<String>>> handle(CustomInvalidValidationFiledConstraintException customInvalidValidationFiledConstraintException){
        return new ResponseEntity<>(customInvalidValidationFiledConstraintException.getErrors(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = CustomInvalidFieldException.class)
    public ResponseEntity<Map<String, String>> handle(CustomInvalidFieldException customInvalidFieldException){
        return new ResponseEntity<>(customInvalidFieldException.getErrors(), HttpStatus.BAD_REQUEST);
    }
}
