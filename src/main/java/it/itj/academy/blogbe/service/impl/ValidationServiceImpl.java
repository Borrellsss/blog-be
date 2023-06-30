package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.output.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.input.ValidationInputDto;
import it.itj.academy.blogbe.dto.output.ValidationOutputDto;
import it.itj.academy.blogbe.dto.output.ValidationPageableOutputDto;
import it.itj.academy.blogbe.entity.Validation;
import it.itj.academy.blogbe.exception.CustomInvalidValidationFiledConstraintException;
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
            return false;
        }
        return true;
    }
    @Override
    public ValidationOutputDto create(ValidationInputDto validationInputDto) {
        if (!isFieldValid(validationInputDto.getField())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation field is not valid: the field must be in the format 'Class.fieldName'");
        }
        Map<String, List<String>> errors = new HashMap<>();
        if (validationInputDto.getCode() == null) {
            addToErrors("code", "Validation code cannot be null", errors);
        } else {
            if (validationInputDto.getCode().length() > 8) {
                addToErrors("code", String.format("Validation code (%s) cannot be greater than 8 characters", validationInputDto.getCode()), errors);
            }
            if (validationRepository.findByCode(validationInputDto.getCode()).isPresent()) {
                addToErrors("code", String.format("Validation code (%s) already exists", validationInputDto.getCode()), errors);
            }
        }
        if (validationInputDto.getField() == null) {
            addToErrors("field", "Validation field cannot be null", errors);
        } else if (validationRepository.findByField(validationInputDto.getField()).isPresent()) {
            addToErrors("field", String.format("Validation field (%s) already exists", validationInputDto.getField()), errors);
        }
        if (validationInputDto.getMin() != null && validationInputDto.getMax() != null) {
            if (validationInputDto.getMin() <= 0) {
                addToErrors("minLength", "Validation min length cannot be less than 1", errors);
            }
            if (validationInputDto.getMax() <= 0) {
                addToErrors("maxLength", "Validation max length cannot be less than 1", errors);
            }
            if (validationInputDto.getMin() > validationInputDto.getMax()) {
                addToErrors("minLength", "Validation min length cannot be greater than max length", errors);
            }
        }
        if (validationInputDto.getRegex() != null) {
            try {
                Pattern.compile(validationInputDto.getRegex());
            } catch (PatternSyntaxException exception) {
                addToErrors("regex", String.format("Validation regex (%s) is not valid", validationInputDto.getRegex()), errors);
            }
        }
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
    public ValidationOutputDto update(String code, ValidationInputDto validationInputDto) {
        Validation validation = validationRepository.findByCode(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with code %s not found", code)));
        if (!isFieldValid(validationInputDto.getField())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation field is not valid: the field must be in the format \"ClassName.fieldName\"");
        }
        Map<String, List<String>> errors = new HashMap<>();
        if (validationInputDto.getCode() == null) {
            addToErrors("code", "Validation code cannot be null", errors);
        } else {
            if (validationInputDto.getCode().length() > 8) {
                addToErrors("code", String.format("Validation code (%s) cannot be greater than 8 characters", validationInputDto.getCode()), errors);
            }
            if (!validationInputDto.getCode().equals(validation.getCode()) && validationRepository.findByCode(validationInputDto.getCode()).isPresent()) {
                addToErrors("code", String.format("Validation code (%s) already exists", validationInputDto.getCode()), errors);
            }
        }
        if (validationInputDto.getField() == null) {
            addToErrors("field", "Validation field cannot be null", errors);
        } else if (!validationInputDto.getField().equals(validation.getField()) && validationRepository.findByField(validationInputDto.getField()).isPresent()) {
            addToErrors("field", String.format("Validation field (%s) already exists", validationInputDto.getField()), errors);
        }
        if (validationInputDto.getMin() != null && validationInputDto.getMax() != null) {
            if (validationInputDto.getMin() <= 0) {
                addToErrors("minLength", "Validation min length cannot be less than 1", errors);
            }
            if (validationInputDto.getMax() <= 0) {
                addToErrors("maxLength", "Validation max length cannot be less than 1", errors);
            }
            if (validationInputDto.getMin() > validationInputDto.getMax()) {
                addToErrors("minLength", "Validation min length cannot be greater than max length", errors);
            }
        }
        if (validationInputDto.getRegex() != null) {
            try {
                Pattern.compile(validationInputDto.getRegex());
            } catch (PatternSyntaxException exception) {
                addToErrors("regex", String.format("Validation regex (%s) is not valid", validationInputDto.getRegex()), errors);
            }
        }
        if (!errors.isEmpty()) {
            throw new CustomInvalidValidationFiledConstraintException(errors);
        }
        validation.setCode(validationInputDto.getCode());
        validation.setField(validationInputDto.getField());
        validation.setNotNull(validationInputDto.isNotNull());
        validation.setNotEmpty(validationInputDto.isNotEmpty());
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
        validationRepository.delete(validation);
    }
}
