package it.itj.academy.blogbe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.itj.academy.blogbe.dto.input.LoginInputDto;
import it.itj.academy.blogbe.dto.input.RegisterInputDto;
import it.itj.academy.blogbe.dto.input.UserInputDto;
import it.itj.academy.blogbe.dto.output.user.LoginOutputDto;
import it.itj.academy.blogbe.dto.output.user.RegisterOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface UserService {
    RegisterOutputDto register(RegisterInputDto registerInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    LoginOutputDto login(LoginInputDto loginInputDto) throws JsonProcessingException;
    UserPageableOutputDto readAll(int page, int size);
    UserOutputDto readById(Long id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    UserOutputDto readByUsername(String username) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    UserOutputDto update(Long id, UserInputDto userInputDto);
    void delete(Long id);
}
