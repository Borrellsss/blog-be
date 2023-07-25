package it.itj.academy.blogbe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.itj.academy.blogbe.dto.input.SignInInputDto;
import it.itj.academy.blogbe.dto.input.SignUpInputDto;
import it.itj.academy.blogbe.dto.input.UserInputDto;
import it.itj.academy.blogbe.dto.output.user.SignInOutputDto;
import it.itj.academy.blogbe.dto.output.user.SignUpOutputDto;
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
    private final int PAGE_SIZE = 30;

    // INSERT
    @PostMapping(value = "/sign-up")
    public ResponseEntity<SignUpOutputDto> signUp(@RequestBody SignUpInputDto signUpInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(userService.signUp(signUpInputDto), HttpStatus.CREATED);
    }
    @PostMapping(value = "/sign-in")
    public ResponseEntity<SignInOutputDto> signIn(@RequestBody SignInInputDto signInInputDto) throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(userService.signIn(signInInputDto), HttpStatus.OK);
    }
    // SELECT
    @GetMapping
    public ResponseEntity<UserPageableOutputDto> readAllByDeletedIsFalseOrderByUsername(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(userService.readAllByDeletedIsFalseOrderByUsername(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/username-contains")
    public ResponseEntity<UserPageableOutputDto> readAllByUsernameContainingAndDeletedIsFalseOrderByUsername(@RequestParam String value, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(userService.readAllByUsernameContainingAndDeletedIsFalseOrderByUsername(value, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserOutputDto> readById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "/username/{username}")
    public ResponseEntity<UserOutputDto> readByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.readByUsername(username), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserOutputDto> update(@PathVariable Long id, @RequestBody UserInputDto userInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(userService.update(id, userInputDto), HttpStatus.OK);
    }
    @PutMapping(value = "/{id}/password")
    public ResponseEntity<UserOutputDto> updatePassword(@PathVariable Long id, @RequestBody UserInputDto userInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(userService.updatePassword(id, userInputDto), HttpStatus.OK);
    }
    @PutMapping(value = "/{id}/block-or-unblock")
    public ResponseEntity<Void> blockOrUnblock(@PathVariable Long id) {
        userService.blockOrUnblock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
