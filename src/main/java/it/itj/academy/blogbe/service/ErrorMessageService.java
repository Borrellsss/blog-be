package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.ErrorMessageInputDto;
import it.itj.academy.blogbe.dto.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.ErrorMessagePageableOutputDto;

public interface ErrorMessageService {
    ErrorMessageOutputDto create(ErrorMessageInputDto errorMessageInputDto);
    ErrorMessagePageableOutputDto readAll(int page, int size);
    ErrorMessageOutputDto readById(Long id);
    ErrorMessageOutputDto update(Long id, ErrorMessageInputDto errorMessageInputDto);
    void delete(Long id);
}
