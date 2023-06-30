package it.itj.academy.blogbe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.itj.academy.blogbe.dto.input.LoginInputDto;
import it.itj.academy.blogbe.dto.input.RegisterInputDto;
import it.itj.academy.blogbe.dto.input.UserInputDto;
import it.itj.academy.blogbe.dto.output.LoginOutputDto;
import it.itj.academy.blogbe.dto.output.RegisterOutputDto;
import it.itj.academy.blogbe.dto.output.UserOutputDto;
import it.itj.academy.blogbe.dto.output.UserPageableOutputDto;

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
