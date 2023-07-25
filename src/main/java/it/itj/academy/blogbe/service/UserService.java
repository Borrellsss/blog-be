package it.itj.academy.blogbe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.itj.academy.blogbe.dto.input.SignInInputDto;
import it.itj.academy.blogbe.dto.input.SignUpInputDto;
import it.itj.academy.blogbe.dto.input.UserInputDto;
import it.itj.academy.blogbe.dto.output.user.SignInOutputDto;
import it.itj.academy.blogbe.dto.output.user.SignUpOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface UserService {
    SignUpOutputDto signUp(SignUpInputDto signUpInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    SignInOutputDto signIn(SignInInputDto signInInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, JsonProcessingException;
    UserPageableOutputDto readAll(int page, int size);
    UserPageableOutputDto readAllByDeletedIsFalseOrderByUsername(int page, int size);
    UserPageableOutputDto readAllByUsernameContainingAndDeletedIsFalseOrderByUsername(String username, int page, int size);
    UserOutputDto readById(Long id);
    UserOutputDto readByUsername(String username);
    UserOutputDto update(Long id, UserInputDto userInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    UserOutputDto updatePassword(Long id, UserInputDto userInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void blockOrUnblock(Long id);
    void delete(Long id);
}
