package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.ErrorMessageInputDto;
import it.itj.academy.blogbe.dto.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.ErrorMessagePageableOutputDto;
import it.itj.academy.blogbe.entity.ErrorMessage;
import it.itj.academy.blogbe.entity.Validation;
import it.itj.academy.blogbe.repository.ErrorMessageRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import it.itj.academy.blogbe.service.ErrorMessageService;
import it.itj.academy.blogbe.util.PageableUtil;
import it.itj.academy.blogbe.util.ValidatorUtil;
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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ErrorMessageServiceImpl implements ErrorMessageService {
    private final ErrorMessageRepository errorMessageRepository;
    private final ValidationRepository validationRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;

    private boolean isErrorTypeValid(String errorType) {
        String[] fieldClass = errorType.split("_");
        if (fieldClass.length != 3) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName(String.format("it.itj.academy.blogbe.dto.%s", fieldClass[0]));
            clazz.getDeclaredField(fieldClass[1]);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            return false;
        }
        return isValidationConstraintPresent(errorType);
    }
    private boolean isValidationConstraintPresent(String errorType) {
        String[] errorTypeParts = errorType.split("_");
        Validation validation = validationRepository.findByField(errorTypeParts[0] + "_" + errorTypeParts[1])
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Validation with field %s not found", errorTypeParts[0] + "_" + errorTypeParts[1])));
        return validation.isNotNull() && errorTypeParts[2].equals(validatorUtil.NOT_NULL) ||
            validation.isNotEmpty() && errorTypeParts[2].equals(validatorUtil.NOT_EMPTY) ||
            validation.getMin() != null && errorTypeParts[2].equals(validatorUtil.MIN) ||
            validation.getMax() != null && errorTypeParts[2].equals(validatorUtil.MAX) ||
            validation.getRegex() != null && errorTypeParts[2].equals(validatorUtil.REGEX) ||
            validation.getMinUpperCaseLetters() != null && errorTypeParts[2].equals(validatorUtil.MIN_UPPERCASE_LETTERS) ||
            validation.getMinLowerCaseLetters() != null && errorTypeParts[2].equals(validatorUtil.MIN_LOWERCASE_LETTERS) ||
            validation.getMinDigits() != null && errorTypeParts[2].equals(validatorUtil.MIN_DIGITS) ||
            validation.getMinSpecialCharacters() != null && errorTypeParts[2].equals(validatorUtil.MIN_SPECIAL_CHARACTERS);
    }
    @Override
    public ErrorMessageOutputDto create(ErrorMessageInputDto errorMessageInputDto) {
        if (!isErrorTypeValid(errorMessageInputDto.getErrorType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Error type %s is not valid: the type must be in the format \"Class_fieldName_CONSTRAINT-TYPE\" and the field must have the corresponding constraint",
                    errorMessageInputDto.getErrorType()));
        }
        ErrorMessage errorMessage = modelMapper.map(errorMessageInputDto, ErrorMessage.class);
        errorMessage.setValidation(validationRepository.findByCode(errorMessageInputDto.getValidationCode())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Validation with code %s not found", errorMessageInputDto.getValidationCode()))));
        ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessageRepository.save(errorMessage), ErrorMessageOutputDto.class);
        errorMessageOutputDto.setValidationCode(errorMessage.getValidation().getCode());
        return errorMessageOutputDto;
    }
    @Override
    public ErrorMessagePageableOutputDto readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ErrorMessage> errorsMessages = errorMessageRepository.findAll(pageable);
        return pageableUtil.errorMessagePageableOutputDto(errorsMessages);
    }
    @Override
    public ErrorMessageOutputDto readById(Long id) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        return modelMapper.map(errorMessage, ErrorMessageOutputDto.class);
    }
    @Override
    public ErrorMessageOutputDto update(Long id, ErrorMessageInputDto errorMessageInputDto) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        if (!errorMessage.getErrorType().equals(errorMessageInputDto.getErrorType())) {
            if (errorMessageRepository.findByErrorType(errorMessageInputDto.getErrorType()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Error message with type %s already exists", errorMessageInputDto.getErrorType()));
            } else {
                errorMessage.setErrorType(errorMessageInputDto.getErrorType());
            }
        }
        errorMessage.setMessage(errorMessageInputDto.getMessage());
        errorMessage.setValidation(validationRepository.findByCode(errorMessageInputDto.getValidationCode())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Validation with code %s not found", errorMessageInputDto.getValidationCode()))));
        ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessageRepository.save(errorMessage), ErrorMessageOutputDto.class);
        errorMessageOutputDto.setValidationCode(errorMessage.getValidation().getCode());
        return errorMessageOutputDto;
    }
    @Override
    public void delete(Long id) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        errorMessageRepository.delete(errorMessage);
    }
}
