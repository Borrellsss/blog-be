package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.TagInputDto;
import it.itj.academy.blogbe.dto.output.tag.TagOutputDto;
import it.itj.academy.blogbe.dto.output.tag.TagPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface TagService {
    TagOutputDto create(TagInputDto tagInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    TagPageableOutputDto readAll(int page, int size);
    TagPageableOutputDto readAllByOrderByName(int page, int size);
    TagOutputDto readById(Long id);
    TagOutputDto readByName(String name);
    TagPageableOutputDto readAllByNameContainingOrderByName(String name, int page, int size);
    TagPageableOutputDto readAllByCategoryNameOrderByName(String categoryName, int page, int size);
    TagOutputDto update(Long id, TagInputDto tagInputDto);
    void delete(Long id);
}
