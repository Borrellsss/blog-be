package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.RoleInputDto;
import it.itj.academy.blogbe.dto.output.role.RoleOutputDto;
import it.itj.academy.blogbe.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private final RoleService roleService;

    // INSERT
    @PostMapping
    public ResponseEntity<RoleOutputDto> create(@RequestBody RoleInputDto roleInputDto) {
        return new ResponseEntity<>(roleService.create(roleInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping
    public ResponseEntity<List<RoleOutputDto>> readAll() {
        return new ResponseEntity<>(roleService.readAll(), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleOutputDto> readById(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "/authority/{authority}")
    public ResponseEntity<RoleOutputDto> readByAuthority(@PathVariable String authority) {
        return new ResponseEntity<>(roleService.readByAuthority(authority), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<RoleOutputDto> update(@PathVariable Long id, @RequestBody RoleInputDto roleInputDto) {
        return new ResponseEntity<>(roleService.update(id, roleInputDto), HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
