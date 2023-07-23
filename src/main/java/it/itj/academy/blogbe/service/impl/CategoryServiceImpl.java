package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.CategoryInputDto;
import it.itj.academy.blogbe.dto.output.category.CategoryOutputDto;
import it.itj.academy.blogbe.dto.output.category.CategoryPageableOutputDto;
import it.itj.academy.blogbe.entity.Category;
import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.repository.CategoryRepository;
import it.itj.academy.blogbe.service.CategoryService;
import it.itj.academy.blogbe.util.OutputDtoResponseUtil;
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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final OutputDtoResponseUtil outputDtoResponseUtil;
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;

    @Override
    public CategoryOutputDto create(CategoryInputDto categoryInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, String> errors = validatorUtil.validate(categoryInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        if (categoryRepository.findByName(categoryInputDto.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Category with name (%s) already exists", categoryInputDto.getName()));
        }
        return modelMapper.map(categoryRepository.save(modelMapper.map(categoryInputDto, Category.class)), CategoryOutputDto.class);
    }
    @Override
    public CategoryPageableOutputDto readAll(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        CategoryPageableOutputDto categoryPageableOutputDto = pageableUtil.categoryPageableOutputDto(categories);
        outputDtoResponseUtil.filter(categoryPageableOutputDto.getCategories());
        return categoryPageableOutputDto;
    }
    @Override
    public CategoryPageableOutputDto readAllOrderByName(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAllByOrderByName(pageable);
        CategoryPageableOutputDto categoryPageableOutputDto = pageableUtil.categoryPageableOutputDto(categories);
        outputDtoResponseUtil.filter(categoryPageableOutputDto.getCategories());
        return categoryPageableOutputDto;
    }
    @Override
    public CategoryOutputDto readById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Category with id (%s) not found", id)));
        return modelMapper.map(category, CategoryOutputDto.class);
    }
    @Override
    public CategoryOutputDto readByName(String name) {
        Category category = categoryRepository.findByName(name)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Category with name (%s) not found", name)));
        return modelMapper.map(category, CategoryOutputDto.class);
    }
    @Override
    public CategoryPageableOutputDto readAllByNameContainsOrderByName(String name, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAllByNameContainsOrderByName(name, pageable);
        CategoryPageableOutputDto categoryPageableOutputDto = pageableUtil.categoryPageableOutputDto(categories);
        outputDtoResponseUtil.filter(categoryPageableOutputDto.getCategories());
        return categoryPageableOutputDto;
    }
    @Override
    public CategoryOutputDto update(Long id, CategoryInputDto categoryInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, String> errors = validatorUtil.validate(categoryInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Category with id (%s) not found", id)));
        if (!category.getName().equals(categoryInputDto.getName()) && categoryRepository.existsByName(categoryInputDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Category with name (%s) already exists", categoryInputDto.getName()));
        }
        category.setName(categoryInputDto.getName());
        return modelMapper.map(categoryRepository.save(category), CategoryOutputDto.class);
    }
    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Category with id (%s) not found", id)));
        categoryRepository.delete(category);
    }
}
