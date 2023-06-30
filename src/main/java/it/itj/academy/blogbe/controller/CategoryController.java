package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.CategoryInputDto;
import it.itj.academy.blogbe.dto.output.CategoryOutputDto;
import it.itj.academy.blogbe.dto.output.CategoryPageableOutputDto;
import it.itj.academy.blogbe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final int PAGE_SIZE = 20;

    @PostMapping
    public ResponseEntity<CategoryOutputDto> create(@RequestBody CategoryInputDto categoryInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(categoryService.create(categoryInputDto), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<CategoryPageableOutputDto> readAll(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(categoryService.readAll(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryOutputDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "/name/{name}")
    public ResponseEntity<CategoryOutputDto> findByName(@PathVariable String name) {
        return new ResponseEntity<>(categoryService.readByName(name), HttpStatus.OK);
    }
    @GetMapping(value = "/like")
    public ResponseEntity<CategoryPageableOutputDto> findByNameContains(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(categoryService.findByNameContains(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryOutputDto> update(@PathVariable Long id, @RequestBody CategoryInputDto categoryInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(categoryService.update(id, categoryInputDto), HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}