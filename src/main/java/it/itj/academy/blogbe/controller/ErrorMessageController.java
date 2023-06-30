package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.ErrorMessageInputDto;
import it.itj.academy.blogbe.dto.output.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.output.ErrorMessagePageableOutputDto;
import it.itj.academy.blogbe.service.ErrorMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/error-messages")
public class ErrorMessageController {
    private final ErrorMessageService errorMessageService;
    private final int PAGE_SIZE = 20;

    // INSERT
    @PostMapping
    public ResponseEntity<ErrorMessageOutputDto> create(@RequestBody ErrorMessageInputDto errorMessageInputDto) {
        return new ResponseEntity<>(errorMessageService.create(errorMessageInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping
    public ResponseEntity<ErrorMessagePageableOutputDto> readAll(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(errorMessageService.readAll(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<ErrorMessageOutputDto> readById(@PathVariable Long id) {
        return new ResponseEntity<>(errorMessageService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "/validation-code/{validationCode}")
    public ResponseEntity<List<ErrorMessageOutputDto>> readByValidationCode(@PathVariable String validationCode) {
        return new ResponseEntity<>(errorMessageService.readByValidationCode(validationCode), HttpStatus.OK);
    }
    @GetMapping(value = "/error-type/{errorType}")
    public ResponseEntity<ErrorMessageOutputDto> readByErrorType(@PathVariable String errorType) {
        return new ResponseEntity<>(errorMessageService.readByErrorType(errorType), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<ErrorMessageOutputDto> update(@PathVariable Long id, @RequestBody ErrorMessageInputDto errorMessageInputDto) {
        return new ResponseEntity<>(errorMessageService.update(id, errorMessageInputDto), HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        errorMessageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
