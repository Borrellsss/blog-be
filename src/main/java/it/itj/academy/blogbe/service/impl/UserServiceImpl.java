package it.itj.academy.blogbe.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.itj.academy.blogbe.dto.*;
import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.repository.RoleRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import it.itj.academy.blogbe.service.UserService;
import it.itj.academy.blogbe.util.JWTUtil;
import it.itj.academy.blogbe.util.PageableUtil;
import it.itj.academy.blogbe.util.ValidatorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ValidationRepository validationRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;

    @Override
    public RegisterOutputDto register(RegisterInputDto registerInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, String> errors = validatorUtil.validate(registerInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        User user = modelMapper.map(registerInputDto, User.class);
        if (registerInputDto.getRoles() == null || registerInputDto.getRoles().isEmpty()) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                user.setRoles(List.of(roleRepository.findByAuthority("ROLE_USER").get()));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Roles is required");
            }
        } else {
            user.setRoles(registerInputDto.getRoles()
                .stream()
                .map(roleId -> roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id %d not found", roleId))))
                .toList());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return modelMapper.map(userRepository.save(user), RegisterOutputDto.class);
    }
    @Override
    public LoginOutputDto login(LoginInputDto loginInputDto) throws JsonProcessingException {
        User user = userRepository.findByUsername(loginInputDto.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username %s not found", loginInputDto.getUsername())));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username %s has been deleted already", loginInputDto.getUsername()));
        }
        if (user.isBlocked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format("This account has been suspended, please contact customer support"));
        }
        if (!bCryptPasswordEncoder.matches(loginInputDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        UserOutputDto userOutputDto = modelMapper.map(user, UserOutputDto.class);
        Map<String, String> privateClaim = Map.of("user", objectMapper.writeValueAsString(userOutputDto));;
        String jwt = jwtUtil.generate(user.getUsername(), privateClaim);
        return new LoginOutputDto(jwt, userOutputDto);
    }
    @Override
    public UserPageableOutputDto readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return pageableUtil.userPageableOutputDto(users);
    }
    @Override
    public UserOutputDto readById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted already", id));
        }
        return modelMapper.map(user, UserOutputDto.class);
    }
    @Override
    public UserOutputDto readByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username %s not found", username)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username %s has been deleted", username));
        }
        return modelMapper.map(user, UserOutputDto.class);
    }
    @Override
    public UserOutputDto update(Long id, UserInputDto userInputDto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted", id));
        }
        if (userInputDto.getRoles() == null || userInputDto.getRoles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Roles is required");
        } else {
            user.setRoles(userInputDto.getRoles()
                .stream()
                .map(roleId -> roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id %d not found", roleId))))
                .toList());
        }
        user.setFirstName(userInputDto.getFirstName());
        user.setLastName(userInputDto.getLastName());
        user.setAge(userInputDto.getAge());
        user.setEmail(userInputDto.getEmail());
        user.setUsername(userInputDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setAvatar(userInputDto.getAvatar());
        return modelMapper.map(userRepository.save(user), UserOutputDto.class);
    }
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted already", id));
        }
        user.setDeleted(true);
        userRepository.save(user);
    }
}
