package it.itj.academy.blogbe.util;

import it.itj.academy.blogbe.entity.ErrorMessage;
import it.itj.academy.blogbe.entity.Validation;
import it.itj.academy.blogbe.repository.ErrorMessageRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@RequiredArgsConstructor
@Component
public class ValidatorUtil {
    private final ValidationRepository validationRepository;
    private final ErrorMessageRepository errorMessageRepository;
    @Value("${validation.type.not-null}")
    public String NOT_NULL;
    @Value("${validation.type.not-empty}")
    public String NOT_EMPTY;
    @Value("${validation.type.min}")
    public String MIN;
    @Value("${validation.type.max}")
    public String MAX;
    @Value("${validation.type.regex}")
    public String REGEX;
    @Value("${validation.type.min-uppercase-letters}")
    public String MIN_UPPERCASE_LETTERS;
    @Value("${validation.type.min-lowercase-letters}")
    public String MIN_LOWERCASE_LETTERS;
    @Value("${validation.type.min-digits}")
    public String MIN_DIGITS;
    @Value("${validation.type.min-special-characters}")
    public String MIN_SPECIAL_CHARACTERS;

    public Map<String, String> validate(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = getClazz(entity);
        List<Validation> validations = validationRepository.findByFieldStartsWith(clazz.getSimpleName());
        Map<String, String> errors = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            for (Validation validation : validations) {
                if (validation.getField().endsWith(field.getName())) {
                    validateField(entity, field, validation, errors);
                }
            }
        }
        return errors;
    }
    private void validateField(Object entity, Field field, Validation validation, Map<String, String> errors) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object value = getGetter(entity, field).invoke(entity);
        if (value == null && validation.getNotNull()) {
            validateNull(entity, field, errors);
        }
        if (value instanceof String s) {
            validateString(entity, field, s, validation, errors);
        } else if (value instanceof Number n) {
            validateNumber(entity, field, n, validation, errors);
        } else if (value instanceof LocalDate d) {
            validateLocalDate(entity, field, d, validation, errors);
        } else if (value instanceof LocalDateTime d) {
            validateLocalDateTime(entity, field, d, validation, errors);
        } else if (value instanceof Collection<?> c) {
            validateCollection(entity, field, c, validation, errors);
        }
    }
    // VALIDATION METHODS
    // NULL VALIDATION
    private void validateNull(Object entity, Field field, Map<String, String> errors) {
        String errorTypePrefix = getErrorTypePrefix(entity, field);
        String message;
        message = errorMessageRepository.findByErrorType(errorTypePrefix + NOT_NULL)
            .map(ErrorMessage::getMessage).orElse("Field cannot be null");
        addError(field.getName(), NOT_NULL, message, errors);
    }
    // STRING VALIDATION
    private void validateString(Object entity, Field field, String value, Validation validation, Map<String, String> errors) {
        String errorTypePrefix = getErrorTypePrefix(entity, field);
        String message;
        if (validation.getNotEmpty() && value.isBlank()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + NOT_EMPTY)
                .map(ErrorMessage::getMessage).orElse("Field cannot be empty");
            addError(field.getName(), NOT_EMPTY, message, errors);
        }
        if (validation.getMin() != null && value.length() < validation.getMin()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN)
                .map(ErrorMessage::getMessage).orElse("Field is too short");
            addError(field.getName(), MIN, message, errors);
        }
        if (validation.getMax() != null && value.length() > validation.getMax()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MAX)
                .map(ErrorMessage::getMessage).orElse("Field is too long");
            addError(field.getName(), MAX, message, errors);
        }
        if (validation.getRegex() != null && !value.isBlank() && !value.matches(validation.getRegex())) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + REGEX)
                .map(ErrorMessage::getMessage).orElse("Field does not match regex");
            addError(field.getName(), REGEX, message, errors);
        }
        if (validation.getMinUpperCaseLetters() != null && value.chars().filter(Character::isUpperCase).count() < validation.getMinUpperCaseLetters()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_UPPERCASE_LETTERS)
                .map(ErrorMessage::getMessage).orElse("Field does not have enough upper case letters");
            addError(field.getName(), MIN_UPPERCASE_LETTERS, message, errors);
        }
        if (validation.getMinLowerCaseLetters() != null && value.chars().filter(Character::isLowerCase).count() < validation.getMinLowerCaseLetters()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_LOWERCASE_LETTERS)
                .map(ErrorMessage::getMessage).orElse("Field does not have enough lower case letters");
            addError(field.getName(), MIN_LOWERCASE_LETTERS, message, errors);
        }
        if (validation.getMinDigits() != null && value.chars().filter(Character::isDigit).count() < validation.getMinDigits()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_DIGITS)
                .map(ErrorMessage::getMessage).orElse("Field does not have enough digits");
            addError(field.getName(), MIN_DIGITS, message, errors);
        }
        if (validation.getMinSpecialCharacters() != null && value.chars().filter(c -> !Character.isLetterOrDigit(c)).count() < validation.getMinSpecialCharacters()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_SPECIAL_CHARACTERS)
                .map(ErrorMessage::getMessage).orElse("Field does not have enough special characters");
            addError(field.getName(), MIN_SPECIAL_CHARACTERS, message, errors);
        }
    }
    // NUMBER VALIDATION
    private void validateNumber(Object entity, Field field, Number value, Validation validation, Map<String, String> errors) {
        String errorTypePrefix = getErrorTypePrefix(entity, field);
        String message;
        if (validation.getMin() != null && value.intValue() < validation.getMin()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN)
                .map(ErrorMessage::getMessage).orElse("Field is too small");
            addError(field.getName(), MIN, message, errors);
        }
        if (validation.getMax() != null && value.intValue() > validation.getMax()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MAX)
                .map(ErrorMessage::getMessage).orElse("Field is too big");
            addError(field.getName(), MAX, message, errors);
        }
    }
    // DATES VALIDATION
    // LOCALDATE VALIDATION
    private void validateLocalDate(Object entity, Field field, LocalDate value, Validation validation, Map<String, String> errors) {
        String errorTypePrefix = getErrorTypePrefix(entity, field);
        String message;
        if (validation.getMin() != null && Period.between(value, LocalDate.now()).getYears() < validation.getMin()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN)
                .map(ErrorMessage::getMessage).orElse(String.format("Field is too early, must be at least %d years old", validation.getMin()));
            addError(field.getName(), MIN, message, errors);
        }
    }
    // LOCALDATETIME VALIDATION
    private void validateLocalDateTime(Object entity, Field field, LocalDateTime value, Validation validation, Map<String, String> errors) {
        String errorTypePrefix = getErrorTypePrefix(entity, field);
        String message;
        if (validation.getMin() != null && Period.between(value.toLocalDate(), LocalDate.now()).getYears() < validation.getMin()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN)
                .map(ErrorMessage::getMessage).orElse(String.format("Field is too early, must be at least %d years old", validation.getMin()));
            addError(field.getName(), MIN, message, errors);
        }
    }
    // COLLECTION VALIDATION
    private void validateCollection(Object entity, Field field, Collection<?> value, Validation validation, Map<String, String> errors) {
        String errorTypePrefix = getErrorTypePrefix(entity, field);
        String message;
        if (validation.getNotEmpty() && value.size() == 0) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + NOT_EMPTY)
                .map(ErrorMessage::getMessage).orElse("Field cannot be empty");
            addError(field.getName(), NOT_EMPTY, message, errors);
        }
        if (validation.getMin() != null && value.size() < validation.getMin()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN)
                .map(ErrorMessage::getMessage).orElse(String.format("Field must have at least %d elements", validation.getMin()));
            addError(field.getName(), MIN, message, errors);
        }
        if (validation.getMax() != null && value.size() > validation.getMax()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + MAX)
                    .map(ErrorMessage::getMessage).orElse(String.format("Field must have at most %d elements", validation.getMax()));
            addError(field.getName(), MAX, message, errors);
        }
    }
    private void addError(String field, String type, String message, Map<String, String> errors) {
        errors.put(String.format("%s (%s)", field, type), message);
    }
    private String getErrorTypePrefix(Object entity, Field field) {
        return getClazz(entity).getSimpleName() + "." + field.getName() + ".";
    }
    private Method getGetter(Object entity, Field field) throws NoSuchMethodException {
        String getter = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        return getClazz(entity).getDeclaredMethod(getter);
    }
    private Class<?> getClazz(Object entity) {
        return entity.getClass();
    }
}
