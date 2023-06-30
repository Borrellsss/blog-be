package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.ErrorMessageInputDto;
import it.itj.academy.blogbe.dto.output.error_message.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.output.error_message.ErrorMessagePageableOutputDto;

import java.util.List;

public interface ErrorMessageService {
    ErrorMessageOutputDto create(ErrorMessageInputDto errorMessageInputDto);
    ErrorMessagePageableOutputDto readAll(int page, int size);
    ErrorMessageOutputDto readById(Long id);
    List<ErrorMessageOutputDto> readByValidationCode(String validationCode);
    ErrorMessageOutputDto readByErrorType(String errorType);
    ErrorMessageOutputDto update(Long id, ErrorMessageInputDto errorMessageInputDto);
    void delete(Long id);
}
