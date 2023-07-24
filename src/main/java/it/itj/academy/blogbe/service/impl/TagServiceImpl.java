package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.TagInputDto;
import it.itj.academy.blogbe.dto.output.tag.TagOutputDto;
import it.itj.academy.blogbe.dto.output.tag.TagPageableOutputDto;
import it.itj.academy.blogbe.entity.Tag;
import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.repository.CategoryRepository;
import it.itj.academy.blogbe.repository.TagRepository;
import it.itj.academy.blogbe.service.TagService;
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
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;

    @Override
    public TagOutputDto create(TagInputDto tagInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, String> errors = validatorUtil.validate(tagInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        if (tagRepository.findByName(tagInputDto.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Tag with name (%s) already exists", tagInputDto.getName()));
        }
        return modelMapper.map(tagRepository.save(modelMapper.map(tagInputDto, Tag.class)), TagOutputDto.class);
    }
    @Override
    public TagPageableOutputDto readAll(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tag> tags = tagRepository.findAll(pageable);
        return pageableUtil.tagPageableOutputDto(tags);
    }
    @Override
    public TagPageableOutputDto readAllByOrderByName(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tag> tags = tagRepository.findAllByOrderByName(pageable);
        return pageableUtil.tagPageableOutputDto(tags);
    }
    @Override
    public TagOutputDto readById(Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Tag with id (%d) not found", id)));
        return modelMapper.map(tag, TagOutputDto.class);
    }
    @Override
    public TagOutputDto readByName(String name) {
        Tag tag = tagRepository.findByName(name)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Tag with name (%s) not found", name)));
        return modelMapper.map(tag, TagOutputDto.class);
    }
    @Override
    public TagPageableOutputDto readAllByNameContainingOrderByName(String name, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tag> tags = tagRepository.findAllByNameContainingOrderByName(name, pageable);
        return pageableUtil.tagPageableOutputDto(tags);
    }
    @Override
    public TagPageableOutputDto readAllByCategoryNameOrderByName(String categoryName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!categoryRepository.existsByName(categoryName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Category with name (%s) not found", categoryName));
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Tag> tags = tagRepository.findAllByCategoriesNameOrderByName(categoryName, pageable);
        return pageableUtil.tagPageableOutputDto(tags);
    }
    @Override
    public TagOutputDto update(Long id, TagInputDto tagInputDto) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Tag with id (%d) not found", id)));
        if (!tag.getName().equals(tagInputDto.getName()) && tagRepository.existsByName(tagInputDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Tag with name (%s) already exists", tagInputDto.getName()));
        }
        tag.setName(tagInputDto.getName());
        return modelMapper.map(tagRepository.save(tag), TagOutputDto.class);
    }
    @Override
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Tag with id (%d) not found", id)));
        tagRepository.delete(tag);
    }
}
