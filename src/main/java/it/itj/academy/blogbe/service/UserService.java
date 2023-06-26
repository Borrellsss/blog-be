package it.itj.academy.blogbe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.itj.academy.blogbe.dto.*;

import java.lang.reflect.InvocationTargetException;

public interface UserService {
    RegisterOutputDto register(RegisterInputDto registerInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    LoginOutputDto login(LoginInputDto loginInputDto) throws JsonProcessingException;
    UserPageableOutputDto readAll(int page, int size);
    UserOutputDto readById(Long id);
    UserOutputDto readByUsername(String username);
    UserOutputDto update(Long id, UserInputDto userInputDto);
    void delete(Long id);
}
