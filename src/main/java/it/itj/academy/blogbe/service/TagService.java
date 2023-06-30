package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.TagInputDto;
import it.itj.academy.blogbe.dto.output.tag.TagOutputDto;
import it.itj.academy.blogbe.dto.output.tag.TagPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface TagService {
    TagOutputDto create(TagInputDto tagInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    TagPageableOutputDto readAll(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    TagOutputDto readById(Long id);
    TagOutputDto readByName(String name);
    TagPageableOutputDto readByNameContains(String name, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    TagPageableOutputDto readByCategoryName(String categoryName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    TagOutputDto update(Long id, TagInputDto tagInputDto);
    void delete(Long id);
}
