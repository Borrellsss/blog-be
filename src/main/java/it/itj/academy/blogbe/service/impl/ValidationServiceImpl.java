package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.ValidationInputDto;
import it.itj.academy.blogbe.dto.ValidationOutputDto;
import it.itj.academy.blogbe.dto.ValidationPageableOutputDto;
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

import java.util.HashMap;
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

    private boolean isFieldValid(String field) {
        String[] fieldClass = field.split("_");
        if (fieldClass.length != 2) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName(String.format("it.itj.academy.blogbe.dto.%s", fieldClass[0]));
            clazz.getDeclaredField(fieldClass[1]);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            return false;
        }
        return true;
    }

    @Override
    public ValidationOutputDto create(ValidationInputDto validationInputDto) {
        if (!isFieldValid(validationInputDto.getField())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation field is not valid: the field must be in the format \"Class_fieldName\"");
        }
        Map<String, String> errors = new HashMap<>();
        if (validationInputDto.getCode() == null) {
            errors.put("code", "Validation code cannot be null");
        } else {
            if (validationInputDto.getCode().length() > 10) {
                errors.put("code", "Validation code cannot be greater than 10 characters");
            }
            if (validationRepository.findByCode(validationInputDto.getCode()).isPresent()) {
                errors.put("code", "Validation code already exists");
            }
        }
        if (validationInputDto.getField() == null) {
            errors.put("field", "Validation field cannot be null");
        } else if (validationRepository.findByField(validationInputDto.getField()).isPresent()) {
            errors.put("field", "Validation field already exists");
        }
        if (validationInputDto.getMin() != null && validationInputDto.getMax() != null) {
            if (validationInputDto.getMin() <= 0) {
                errors.put("minLength", "Validation min length cannot be less than 1");
            }
            if (validationInputDto.getMax() <= 0) {
                errors.put("maxLength", "Validation max length cannot be less than 1");
            }
            if (validationInputDto.getMin() > validationInputDto.getMax()) {
                errors.put("minLength", "Validation min length cannot be greater than max length");
            }
        }
        if (validationInputDto.getRegex() != null) {
            try {
                Pattern.compile(validationInputDto.getRegex());
            } catch (PatternSyntaxException exception) {
                errors.put("regex", "Validation regex is not valid");
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
    public ValidationOutputDto readByCode(String code) {
        Validation validation = validationRepository.findByCode(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with code %s not found", code)));
        ValidationOutputDto validationOutputDto = modelMapper.map(validation, ValidationOutputDto.class);
//        validationOutputDto.setErrorMessages(validation.getErrorMessages().stream()
//            .map(errorMessage -> modelMapper.map(errorMessage, ErrorMessageOutputDto.class))
//            .toList());
        return validationOutputDto;
    }
    @Override
    public ValidationOutputDto readByFiled(String field) {
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
        if (isFieldValid(validationInputDto.getField())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation field is not valid: the field must be in the format \"ClassName_fieldName\"");
        }
        Map<String, String> errors = new HashMap<>();
        if (validationInputDto.getCode() == null) {
            errors.put("code", "Validation code cannot be null");
        } else {
            if (validationInputDto.getCode().length() > 10) {
                errors.put("code", "Validation code cannot be greater than 10 characters");
            }
            if (!validationInputDto.getCode().equals(validation.getCode()) && validationRepository.findByCode(validationInputDto.getCode()).isPresent()) {
                errors.put("code", "Validation code already exists");
            }
        }
        if (validationInputDto.getField() == null) {
            errors.put("field", "Validation field cannot be null");
        } else if (!validationInputDto.getField().equals(validation.getField()) && validationRepository.findByField(validationInputDto.getField()).isPresent()) {
            errors.put("field", "Validation field already exists");
        }
        if (validationInputDto.getMin() <= 0) {
            errors.put("minLength", "Validation min length cannot be less than 1");
        }
        if (validationInputDto.getMax() <= 0) {
            errors.put("maxLength", "Validation max length cannot be less than 1");
        }
        if (validationInputDto.getMin() > validationInputDto.getMax()) {
            errors.put("minLength", "Validation min length cannot be greater than max length");
        }
        if (validationInputDto.getRegex() != null) {
            try {
                Pattern.compile(validationInputDto.getRegex());
            } catch (PatternSyntaxException exception) {
                errors.put("regex", "Validation regex is not valid");
            }
        }
        if (!errors.isEmpty()) {
            throw new CustomInvalidValidationFiledConstraintException(errors);
        }
        validation.setCode(validationInputDto.getCode());
        validation.setField(validationInputDto.getField());
        validation.setNullable(validationInputDto.isNullable());
        validation.setMin(validationInputDto.getMin());
        validation.setMax(validationInputDto.getMax());
        validation.setRegex(validationInputDto.getRegex());
        validation.setMinUpperCaseLetters(validationInputDto.getMinUpperCaseLetters());
        validation.setMinLowerCaseLetters(validationInputDto.getMinLowerCaseLetters());
        validation.setMinDigits(validationInputDto.getMinDigits());
        validation.setMinSpecialCharacters(validationInputDto.getMinSpecialCharacters());
        ValidationOutputDto validationOutputDto = modelMapper.map(validationRepository.save(validation), ValidationOutputDto.class);
        validationOutputDto.setErrorMessages(validation.getErrorMessages().stream()
            .map(errorMessage -> modelMapper.map(errorMessage, ErrorMessageOutputDto.class))
            .toList());
        return validationOutputDto;
    }
    @Override
    public void delete(String code) {
        Validation validation = validationRepository.findByCode(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Validation with code %s not found", code)));
        validationRepository.delete(validation);
    }
}
