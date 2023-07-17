package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.TagInputDto;
import it.itj.academy.blogbe.dto.output.tag.TagOutputDto;
import it.itj.academy.blogbe.dto.output.tag.TagPageableOutputDto;
import it.itj.academy.blogbe.service.TagService;
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
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;
    private final int PAGE_SIZE = 40;

    // INSERT
    @PostMapping
    public ResponseEntity<TagOutputDto> create(@RequestBody TagInputDto tagInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(tagService.create(tagInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping
    public ResponseEntity<TagPageableOutputDto> readAll(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(tagService.readAll(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<TagOutputDto> readById(@PathVariable Long id) {
        return new ResponseEntity<>(tagService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "/name/{name}")
    public ResponseEntity<TagOutputDto> readByName(@PathVariable String name) {
        return new ResponseEntity<>(tagService.readByName(name), HttpStatus.OK);
    }
    @GetMapping(value = "/like")
    public ResponseEntity<TagPageableOutputDto> readByNameContains(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(tagService.readByNameContains(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category/{categoryName}")
    public ResponseEntity<TagPageableOutputDto> readByCategoryName(@PathVariable String categoryName, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(tagService.readByCategoryName(categoryName, page, pageable.getPageSize()), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<TagOutputDto> update(@PathVariable Long id, @RequestBody TagInputDto tagInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(tagService.update(id, tagInputDto), HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
