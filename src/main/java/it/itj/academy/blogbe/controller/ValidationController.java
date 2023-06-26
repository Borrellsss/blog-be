package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.ValidationInputDto;
import it.itj.academy.blogbe.dto.ValidationOutputDto;
import it.itj.academy.blogbe.dto.ValidationPageableOutputDto;
import it.itj.academy.blogbe.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/validations")
public class ValidationController {
    private final ValidationService validationService;
    private final int PAGE_SIZE = 20;

    // INSERT
    @PostMapping
    public ResponseEntity<ValidationOutputDto> create(@RequestBody ValidationInputDto validationInputDto) {
        return new ResponseEntity<>(validationService.create(validationInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping
    public ResponseEntity<ValidationPageableOutputDto> readAll(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(validationService.readAll(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{code}")
    public ResponseEntity<ValidationOutputDto> readByCode(@PathVariable String code) {
        return new ResponseEntity<>(validationService.readByCode(code), HttpStatus.OK);
    }
    @GetMapping(value = "/field/{field}")
    public ResponseEntity<ValidationOutputDto> readByFiled(@PathVariable String field) {
        return new ResponseEntity<>(validationService.readByFiled(field), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{code}")
    public ResponseEntity<ValidationOutputDto> update(@PathVariable String code, @RequestBody ValidationInputDto validationInputDto) {
        return new ResponseEntity<>(validationService.update(code, validationInputDto), HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping(value = "/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        validationService.delete(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
