package it.itj.academy.blogbe.util;

import it.itj.academy.blogbe.entity.ErrorMessage;
import it.itj.academy.blogbe.entity.Validation;
import it.itj.academy.blogbe.repository.ErrorMessageRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String errorTypePrefix = getClazz(entity).getSimpleName() + "_" + field.getName() + "_";
        String message;
        if (value == null && validation.isNotNull()) {
            message = errorMessageRepository.findByErrorType(errorTypePrefix + NOT_NULL)
                .map(ErrorMessage::getMessage).orElse("Field cannot be null");
            addError(field.getName(), NOT_NULL, message, errors);
        }
        if (value instanceof String s) {
            if (s.isBlank() && validation.isNotEmpty()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + NOT_EMPTY)
                    .map(ErrorMessage::getMessage).orElse("Field cannot be empty");
                addError(field.getName(), NOT_EMPTY, message, errors);
            }
            if (validation.getMin() != null && s.length() < validation.getMin()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN)
                    .map(ErrorMessage::getMessage).orElse("Field is too short");
                addError(field.getName(), MIN, message, errors);
            }
            if (validation.getMax() != null && s.length() > validation.getMax()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MAX)
                    .map(ErrorMessage::getMessage).orElse("Field is too long");
                addError(field.getName(), MAX, message, errors);
            }
            if (validation.getRegex() != null && !s.matches(validation.getRegex())) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + REGEX)
                    .map(ErrorMessage::getMessage).orElse("Field does not match regex");
                addError(field.getName(), REGEX, message, errors);
            }
            if (validation.getMinUpperCaseLetters() != null && s.chars().filter(Character::isUpperCase).count() < validation.getMinUpperCaseLetters()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_UPPERCASE_LETTERS)
                    .map(ErrorMessage::getMessage).orElse("Field does not have enough upper case letters");
                addError(field.getName(), MIN_UPPERCASE_LETTERS, message, errors);
            }
            if (validation.getMinLowerCaseLetters() != null && s.chars().filter(Character::isLowerCase).count() < validation.getMinLowerCaseLetters()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_LOWERCASE_LETTERS)
                    .map(ErrorMessage::getMessage).orElse("Field does not have enough lower case letters");
                addError(field.getName(), MIN_LOWERCASE_LETTERS, message, errors);
            }
            if (validation.getMinDigits() != null && s.chars().filter(Character::isDigit).count() < validation.getMinDigits()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_DIGITS)
                    .map(ErrorMessage::getMessage).orElse("Field does not have enough digits");
                addError(field.getName(), MIN_DIGITS, message, errors);
            }
            if (validation.getMinSpecialCharacters() != null && s.chars().filter(c -> !Character.isLetterOrDigit(c)).count() < validation.getMinSpecialCharacters()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN_SPECIAL_CHARACTERS)
                    .map(ErrorMessage::getMessage).orElse("Field does not have enough special characters");
                addError(field.getName(), MIN_SPECIAL_CHARACTERS, message, errors);
            }
        } else if (value instanceof Number n) {
            if (validation.getMin() != null && n.intValue() < validation.getMin()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MIN)
                    .map(ErrorMessage::getMessage).orElse("Field is too small");
                addError(field.getName(), MIN, message, errors);
            }
            if (validation.getMax() != null && n.intValue() > validation.getMax()) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + MAX)
                    .map(ErrorMessage::getMessage).orElse("Field is too big");
                addError(field.getName(), MAX, message, errors);
            }
        } else if (value instanceof Collection<?> c) {
            if (validation.isNotEmpty() && c.size() == 0) {
                message = errorMessageRepository.findByErrorType(errorTypePrefix + NOT_EMPTY)
                    .map(ErrorMessage::getMessage).orElse("Field cannot be empty");
                addError(field.getName(), NOT_EMPTY, message, errors);
            }
        }
    }
    private void addError(String field, String type, String message, Map<String, String> errors) {
        errors.put(String.format("%s (%s)", field, type), message);
    }
    private Method getGetter(Object entity, Field field) throws NoSuchMethodException {
        String getter = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
        return getClazz(entity).getDeclaredMethod(getter);
    }
    private Class<?> getClazz(Object entity) {
        return entity.getClass();
    }
}
