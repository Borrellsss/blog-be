package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.ErrorMessageInputDto;
import it.itj.academy.blogbe.dto.output.error_message.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.output.error_message.ErrorMessagePageableOutputDto;
import it.itj.academy.blogbe.entity.ErrorMessage;
import it.itj.academy.blogbe.entity.Validation;
import it.itj.academy.blogbe.repository.ErrorMessageRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import it.itj.academy.blogbe.service.ErrorMessageService;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ErrorMessageServiceImpl implements ErrorMessageService {
    private final ErrorMessageRepository errorMessageRepository;
    private final ValidationRepository validationRepository;
    private final ModelMapper modelMapper;
    private final PageableUtil pageableUtil;

    private boolean isErrorTypeValid(String errorType) {
        String[] errorTypeFormat = errorType.split("\\.");
        if (errorTypeFormat.length != 3) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName(String.format("it.itj.academy.blogbe.dto.input.%s", errorTypeFormat[0]));
            clazz.getDeclaredField(errorTypeFormat[1]);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            return false;
        }
        return isValidationConstraintPresent(errorTypeFormat);
    }
    private boolean isValidationConstraintPresent(String[] errorTypeFormat) {
        Validation validation = validationRepository.findByField(errorTypeFormat[0] + "." + errorTypeFormat[1])
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Validation with field %s not found", errorTypeFormat[0] + "." + errorTypeFormat[1])));
        try {
            Class<? extends Validation> clazz = validation.getClass();
            Field field = clazz.getDeclaredField(errorTypeFormat[2]);
            String getter = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
            Method method = clazz.getDeclaredMethod(getter);
            return method.invoke(validation) != null && !method.invoke(validation).equals(false);
        } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return false;
        }
    }
    @Override
    public ErrorMessageOutputDto create(ErrorMessageInputDto errorMessageInputDto) {
        if (!isErrorTypeValid(errorMessageInputDto.getErrorType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Error type %s is not valid: the type must be in the format \"Class.fieldName.constraintType\" and the field must have the corresponding constraint",
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
        Page<ErrorMessage> errorMessages = errorMessageRepository.findAll(pageable);
        return pageableUtil.errorMessagePageableOutputDto(errorMessages);
    }
    @Override
    public ErrorMessageOutputDto readById(Long id) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessage, ErrorMessageOutputDto.class);
        errorMessageOutputDto.setValidationCode(errorMessage.getValidation().getCode());
        return errorMessageOutputDto;
    }
    @Override
    public List<ErrorMessageOutputDto> readByValidationCode(String validationCode) {
        List<ErrorMessage> errorMessages = errorMessageRepository.findByValidationCode(validationCode);
        return errorMessages.stream()
            .map(errorMessage -> {
                ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessage, ErrorMessageOutputDto.class);
                errorMessageOutputDto.setValidationCode(errorMessage.getValidation().getCode());
                return errorMessageOutputDto;
            })
            .toList();
    }
    @Override
    public ErrorMessageOutputDto readByErrorType(String errorType) {
        ErrorMessage errorMessage = errorMessageRepository.findByErrorType(errorType)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with type %s not found", errorType)));
        ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessage, ErrorMessageOutputDto.class);
        errorMessageOutputDto.setValidationCode(errorMessage.getValidation().getCode());
        return errorMessageOutputDto;
    }
    @Override
    public ErrorMessageOutputDto update(Long id, ErrorMessageInputDto errorMessageInputDto) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        errorMessage.setMessage(errorMessageInputDto.getMessage());
        errorMessageRepository.save(errorMessage);
        errorMessageRepository.flush();
        ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessageRepository.findById(id), ErrorMessageOutputDto.class);
        return modelMapper.map(errorMessageOutputDto, ErrorMessageOutputDto.class);
    }
    @Override
    public ErrorMessageOutputDto update(String errorType, String validationCode, ErrorMessageInputDto errorMessageInputDto) {
        ErrorMessage errorMessage = errorMessageRepository.findByErrorTypeAndValidationCode(errorType, validationCode)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with type %s and validation code %s not found", errorType, validationCode)));
        errorMessage.setMessage(errorMessageInputDto.getMessage());
        errorMessageRepository.save(errorMessage);
        errorMessageRepository.flush();
        ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessageRepository.findByErrorType(errorType), ErrorMessageOutputDto.class);
        return modelMapper.map(errorMessageOutputDto, ErrorMessageOutputDto.class);
    }
    @Override
    public void delete(Long id) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        errorMessageRepository.delete(errorMessage);
    }
}
