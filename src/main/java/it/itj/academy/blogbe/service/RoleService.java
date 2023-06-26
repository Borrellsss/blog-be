package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.RoleInputDto;
import it.itj.academy.blogbe.dto.RoleOutputDto;

import java.util.List;

public interface RoleService {
    RoleOutputDto create(RoleInputDto roleInputDto);
    List<RoleOutputDto> readAll();
    RoleOutputDto readById(Long id);
    RoleOutputDto readByAuthority(String authority);
    RoleOutputDto update(Long id, RoleInputDto roleInputDto);
    void delete(Long id);
}
