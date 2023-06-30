package it.itj.academy.blogbe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.itj.academy.blogbe.dto.input.LoginInputDto;
import it.itj.academy.blogbe.dto.input.RegisterInputDto;
import it.itj.academy.blogbe.dto.input.UserInputDto;
import it.itj.academy.blogbe.dto.output.user.LoginOutputDto;
import it.itj.academy.blogbe.dto.output.user.RegisterOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserPageableOutputDto;
import it.itj.academy.blogbe.service.UserService;
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
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final int PAGE_SIZE = 20;

    // INSERT
    @PostMapping(value = "/register")
    public ResponseEntity<RegisterOutputDto> register(@RequestBody RegisterInputDto registerInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(userService.register(registerInputDto), HttpStatus.CREATED);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<LoginOutputDto> login(@RequestBody LoginInputDto loginInputDto) throws JsonProcessingException {
        return new ResponseEntity<>(userService.login(loginInputDto), HttpStatus.OK);
    }
    // SELECT
    @GetMapping
    public ResponseEntity<UserPageableOutputDto> readAll(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(userService.readAll(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserOutputDto> readById(@PathVariable Long id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(userService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "/username/{username}")
    public ResponseEntity<UserOutputDto> readByUsername(@PathVariable String username) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(userService.readByUsername(username), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserOutputDto> update(@PathVariable Long id, @RequestBody UserInputDto userInputDto) {
        return new ResponseEntity<>(userService.update(id, userInputDto), HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
