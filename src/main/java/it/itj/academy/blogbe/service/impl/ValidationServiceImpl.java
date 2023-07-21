package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.output.validation.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.input.ValidationInputDto;
import it.itj.academy.blogbe.dto.output.validation.ValidationOutputDto;
import it.itj.academy.blogbe.dto.output.validation.ValidationPageableOutputDto;
import it.itj.academy.blogbe.entity.ErrorMessage;
import it.itj.academy.blogbe.entity.Validation;
import it.itj.academy.blogbe.exception.CustomInvalidValidationFiledConstraintException;
import it.itj.academy.blogbe.repository.ErrorMessageRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import it.itj.academy.blogbe.service.ValidationService;
import it.itj.academy.blogbe.util.PageableUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ValidationServiceImpl implements ValidationService {
    private final ValidationRepository validationRepository;
    private final ErrorMessageRepository errorMessageRepository;
    private final ModelMapper modelMapper;
    private final PageableUtil pageableUtil;

    private void addToErrors(String field, String message, Map<String, List<String>> errors) {
        if (errors.containsKey(field)) {
            errors.get(field).add(message);
        } else {
            List<String> messages = new ArrayList<>();
            messages.add(message);
            errors.put(field, messages);
        }
    }
    private boolean isFieldValid(String field) {
        String[] fieldClass = field.split("\\.");
        if (fieldClass.length != 2) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName(String.format("it.itj.academy.blogbe.dto.input.%s", fieldClass[0]));
            clazz.getDeclaredField(fieldClass[1]);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void validateCode(ValidationInputDto validationInputDto, Map<String, List<String>> errors) {
        if (validationInputDto.getCode() == null) {
            addToErrors("code", "Validation code cannot be null", errors);
        } else {
            if (validationInputDto.getCode().length() > 8) {
                addToErrors("code", String.format("Validation code (%s) cannot be greater than 8 characters", validationInputDto.getCode()), errors);
            }
            if (validationRepository.existsByCode(validationInputDto.getCode())) {
                addToErrors("code", String.format("Validation code (%s) already exists", validationInputDto.getCode()), errors);
            }
        }
    }
    private void validateField(ValidationInputDto validationInputDto, Map<String, List<String>> errors) {
        if (validationInputDto.getField() == null) {
            addToErrors("field", "Validation field cannot be null", errors);
        } else if (validationRepository.existsByField(validationInputDto.getField())) {
            addToErrors("field", String.format("Validation field (%s) already exists", validationInputDto.getField()), errors);
        }
    }
    private void validateMinAndMax(ValidationInputDto validationInputDto, Map<String, List<String>> errors) {
        if (validationInputDto.getMin() != null && validationInputDto.getMax() != null) {
            if (validationInputDto.getMin() <= 0) {
                addToErrors("minLength", "Validation min cannot be less than 1", errors);
            }
            if (validationInputDto.getMax() <= 0) {
                addToErrors("maxLength", "Validation max cannot be less than 1", errors);
            }
            if (validationInputDto.getMin() > validationInputDto.getMax()) {
                addToErrors("minLength", "Validation min cannot be greater than max", errors);
            }
        } else if (validationInputDto.getMin() != null) {
            if (validationInputDto.getMin() <= 0) {
                addToErrors("minLength", "Validation min cannot be less than 1", errors);
            }
        } else if (validationInputDto.getMax() != null) {
            if (validationInputDto.getMax() <= 0) {
                addToErrors("maxLength", "Validation max cannot be less than 1", errors);
            }
        }
    }
    private void validateRegex(ValidationInputDto validationInputDto, Map<String, List<String>> errors) {
        if (validationInputDto.getRegex() != null) {
            try {
                Pattern.compile(validationInputDto.getRegex());
            } catch (PatternSyntaxException exception) {
                addToErrors("regex", String.format("Validation regex (%s) is not valid", validationInputDto.getRegex()), errors);
            }
        }
    }
    private void removeErrorMessages(Validation validation, String suffix) {
        List<ErrorMessage> errorMessages = validation.getErrorMessages().stream()
            .filter(errorMessage -> errorMessage.getErrorType().endsWith(suffix))
            .toList();
        validation.getErrorMessages().removeAll(errorMessages);
        validationRepository.save(validation);
        errorMessageRepository.deleteAll(errorMessages);
    }
    @Override
    public ValidationOutputDto create(ValidationInputDto validationInputDto) {
        if (!isFieldValid(validationInputDto.getField())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation field is not valid: the field must be in the format 'Class.fieldName'");
        }
        Map<String, List<String>> errors = new HashMap<>();
        validateCode(validationInputDto, errors);
        validateField(validationInputDto, errors);
        validateMinAndMax(validationInputDto, errors);
        validateRegex(validationInputDto, errors);
        if (!errors.isEmpty()) {
            throw new CustomInvalidValidationFiledConstraintException(errors);
        }
        return modelMapper.map(validationRepository.save(modelMapper.map(validationInputDto, Validation.class)), ValidationOutputDto.class);
    }
    @Override
    public ValidationPageableOutputDto readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Validation> validations = validationRepository.findAll(pageable);
        return pageableUtil.validationPageableOutputDto(validations);
    }
    @Override
    public ValidationOutputDto readById(Long id) {
        Validation validation = validationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with id %d not found", id)));
        return modelMapper.map(validation, ValidationOutputDto.class);
    }
    @Override
    public ValidationOutputDto readByCode(String code) {
        Validation validation = validationRepository.findByCode(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with code %s not found", code)));
        return modelMapper.map(validation, ValidationOutputDto.class);
    }
    @Override
    public ValidationOutputDto readByField(String field) {
        Validation validation = validationRepository.findByField(field)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with field %s not found", field)));
        ValidationOutputDto validationOutputDto = modelMapper.map(validation, ValidationOutputDto.class);
        validationOutputDto.setErrorMessages(validation.getErrorMessages().stream()
            .map(errorMessage -> modelMapper.map(errorMessage, ErrorMessageOutputDto.class))
            .toList());
        return validationOutputDto;
    }
    @Override
    public List<ValidationOutputDto> readByFieldStartsWith(String field) {
        return validationRepository.findByFieldStartsWith(field)
            .stream()
            .map(validation -> modelMapper.map(validation, ValidationOutputDto.class))
            .toList();
    }
    @Override
    public ValidationOutputDto update(String code, ValidationInputDto validationInputDto) {
        Validation validation = validationRepository.findByCode(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with code %s not found", code)));
        Map<String, List<String>> errors = new HashMap<>();
        validateMinAndMax(validationInputDto, errors);
        validateRegex(validationInputDto, errors);
        if (!errors.isEmpty()) {
            throw new CustomInvalidValidationFiledConstraintException(errors);
        }
        if (validation.getNotNull() && !validationInputDto.getNotNull()) {
            removeErrorMessages(validation, "notNull");
        }
        if (validation.getNotEmpty() && !validationInputDto.getNotEmpty()) {
            removeErrorMessages(validation, "notEmpty");
        }
        if (validation.getMin() != null && validationInputDto.getMin() == null) {
            removeErrorMessages(validation, "min");
        }
        if (validation.getMax() != null && validationInputDto.getMax() == null) {
            removeErrorMessages(validation, "max");
        }
        if (validation.getRegex() != null && validationInputDto.getRegex() == null) {
            removeErrorMessages(validation, "regex");
        }
        if (validation.getMinUpperCaseLetters() != null && validationInputDto.getMinUpperCaseLetters() == null) {
            removeErrorMessages(validation, "minUpperCaseLetters");
        }
        if (validation.getMinLowerCaseLetters() != null && validationInputDto.getMinLowerCaseLetters() == null) {
            removeErrorMessages(validation, "minLowerCaseLetters");
        }
        if (validation.getMinDigits() != null && validationInputDto.getMinDigits() == null) {
            removeErrorMessages(validation, "minDigits");
        }
        if (validation.getMinSpecialCharacters() != null && validationInputDto.getMinSpecialCharacters() == null) {
            removeErrorMessages(validation, "minSpecialCharacters");
        }
        validation.setNotNull(validationInputDto.getNotNull());
        validation.setNotEmpty(validationInputDto.getNotEmpty());
        validation.setMin(validationInputDto.getMin());
        validation.setMax(validationInputDto.getMax());
        validation.setRegex(validationInputDto.getRegex());
        validation.setMinUpperCaseLetters(validationInputDto.getMinUpperCaseLetters());
        validation.setMinLowerCaseLetters(validationInputDto.getMinLowerCaseLetters());
        validation.setMinDigits(validationInputDto.getMinDigits());
        validation.setMinSpecialCharacters(validationInputDto.getMinSpecialCharacters());
        return modelMapper.map(validationRepository.findByCode(validationRepository.save(validation).getCode()), ValidationOutputDto.class);
    }
    @Override
    public void delete(String code) {
        Validation validation = validationRepository.findByCode(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with code %s not found", code)));
//        List<ErrorMessage> errorMessages = errorMessageRepository.
        validationRepository.delete(validation);
    }
}
