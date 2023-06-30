package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.ValidationInputDto;
import it.itj.academy.blogbe.dto.output.validation.ValidationOutputDto;
import it.itj.academy.blogbe.dto.output.validation.ValidationPageableOutputDto;

public interface ValidationService {
    ValidationOutputDto create(ValidationInputDto validationInputDto);
    ValidationPageableOutputDto readAll(int page, int size);
    ValidationOutputDto readById(Long id);
    ValidationOutputDto readByCode(String code);
    ValidationOutputDto readByField(String field);
    ValidationOutputDto update(String code, ValidationInputDto validationInputDto);
    void delete(String code);
}
