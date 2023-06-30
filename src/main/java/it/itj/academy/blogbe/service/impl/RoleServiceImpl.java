package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.RoleInputDto;
import it.itj.academy.blogbe.dto.output.RoleOutputDto;
import it.itj.academy.blogbe.entity.Role;
import it.itj.academy.blogbe.repository.RoleRepository;
import it.itj.academy.blogbe.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoleOutputDto create(RoleInputDto roleInputDto) {
        return modelMapper.map(roleRepository.save(modelMapper.map(roleInputDto, Role.class)), RoleOutputDto.class);
    }
    @Override
    public List<RoleOutputDto> readAll() {
        return roleRepository.findAll()
            .stream()
            .map(role -> modelMapper.map(role, RoleOutputDto.class))
            .toList();
    }
    @Override
    public RoleOutputDto readById(Long id) {
        return roleRepository.findById(id)
            .map(role -> modelMapper.map(role, RoleOutputDto.class))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id %d not found", id)));
    }
    @Override
    public RoleOutputDto readByAuthority(String authority) {
        return roleRepository.findByAuthority(authority)
            .map(role -> modelMapper.map(role, RoleOutputDto.class))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with authority %s not found", authority)));
    }
    @Override
    public RoleOutputDto update(Long id, RoleInputDto roleInputDto) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id %d not found", id)));
        role.setAuthority(roleInputDto.getAuthority());
        return modelMapper.map(role, RoleOutputDto.class);
    }
    @Override
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id %d not found", id)));
        roleRepository.delete(role);
    }
}
