package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.CategoryInputDto;
import it.itj.academy.blogbe.dto.output.category.CategoryOutputDto;
import it.itj.academy.blogbe.dto.output.category.CategoryPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface CategoryService {
    CategoryOutputDto create(CategoryInputDto categoryInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    CategoryPageableOutputDto readAll(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    CategoryPageableOutputDto readAllOrderByName(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    CategoryOutputDto readById(Long id);
    CategoryOutputDto readByName(String name);
    CategoryPageableOutputDto readAllByNameContainingOrderByName(String name, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    CategoryOutputDto update(Long id, CategoryInputDto categoryInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void delete(Long id);
}
