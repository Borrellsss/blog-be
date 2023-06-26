package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.ValidationInputDto;
import it.itj.academy.blogbe.dto.ValidationOutputDto;
import it.itj.academy.blogbe.dto.ValidationPageableOutputDto;

public interface ValidationService {
    ValidationOutputDto create(ValidationInputDto validationInputDto);
    ValidationPageableOutputDto readAll(int page, int size);
    ValidationOutputDto readById(Long id);
    ValidationOutputDto readByCode(String code);
    ValidationOutputDto readByFiled(String field);
    ValidationOutputDto update(String code, ValidationInputDto validationInputDto);
    void delete(String code);
}
