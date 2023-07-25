package it.itj.academy.blogbe.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.itj.academy.blogbe.dto.input.SignInInputDto;
import it.itj.academy.blogbe.dto.input.SignUpInputDto;
import it.itj.academy.blogbe.dto.input.UserInputDto;
import it.itj.academy.blogbe.dto.output.user.SignInOutputDto;
import it.itj.academy.blogbe.dto.output.user.SignUpOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserPageableOutputDto;
import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.repository.RoleRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import it.itj.academy.blogbe.service.UserService;
import it.itj.academy.blogbe.util.JWTUtil;
import it.itj.academy.blogbe.util.PageableUtil;
import it.itj.academy.blogbe.util.ValidatorUtil;
import it.itj.academy.blogbe.util.email.user.UserBlockedOrUnblockedEmailUtil;
import it.itj.academy.blogbe.util.email.user.UserDeletedEmailUtil;
import it.itj.academy.blogbe.util.email.user.UserSignUpEmailUtil;
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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;
    private final UserSignUpEmailUtil userSignUpEmailUtil;
    private final UserBlockedOrUnblockedEmailUtil userBlockedOrUnblockedEmailUtil;
    private final UserDeletedEmailUtil userDeletedEmailUtil;

    @Override
    public SignUpOutputDto signUp(SignUpInputDto signUpInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        User user = modelMapper.map(signUpInputDto, User.class);
        if (signUpInputDto.getRole() == null) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                user.setRole(roleRepository.findByAuthority("ROLE_USER").get());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
            }
        } else {
            user.setRole(roleRepository.findById(signUpInputDto.getRole())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id %d not found", signUpInputDto.getRole()))));
        }
        Map<String, String> errors = validatorUtil.validate(signUpInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        if (userRepository.findByUsername(signUpInputDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Username (%s) already exists", signUpInputDto.getUsername()));
        }
        if (userRepository.findByEmail(signUpInputDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Email (%s) already exists", signUpInputDto.getEmail()));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            userSignUpEmailUtil.sendEmail(user.getEmail(), user);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
        return modelMapper.map(userRepository.save(user), SignUpOutputDto.class);
    }
    @Override
    public SignInOutputDto signIn(SignInInputDto signInInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, JsonProcessingException {
        Map<String, String> errors = validatorUtil.validate(signInInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        User user = userRepository.findByUsername(signInInputDto.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username %s not found", signInInputDto.getUsername())));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username %s has been deleted", signInInputDto.getUsername()));
        }
        if (user.isBlocked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format("This account has been suspended, please contact customer support"));
        }
        if (!bCryptPasswordEncoder.matches(signInInputDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        UserOutputDto userOutputDto = modelMapper.map(user, UserOutputDto.class);
        Map<String, String> privateClaim = Map.of("user", objectMapper.writeValueAsString(userOutputDto));
        String jwt = jwtUtil.generate(user.getUsername(), privateClaim);
        return new SignInOutputDto(jwt);
    }
    @Override
    public UserPageableOutputDto readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return pageableUtil.userPageableOutputDto(users);
    }
    @Override
    public UserPageableOutputDto readAllByDeletedIsFalseOrderByUsername(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAllByDeletedIsFalseOrderByUsername(pageable);
        return pageableUtil.userPageableOutputDto(users);
    }
    @Override
    public UserPageableOutputDto readAllByUsernameContainingAndDeletedIsFalseOrderByUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAllByUsernameContainingAndDeletedIsFalseOrderByUsername(username, pageable);
        return pageableUtil.userPageableOutputDto(users);
    }
    @Override
    public UserOutputDto readById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted", id));
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
    public UserOutputDto update(Long id, UserInputDto userInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted", id));
        }
        if (userInputDto.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        } else {
            user.setRole(roleRepository.findById(userInputDto.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id %d not found", userInputDto.getRole()))));
        }
        Map<String, String> errors = validatorUtil.validate(userInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        user.setFirstName(userInputDto.getFirstName());
        user.setLastName(userInputDto.getLastName());
        user.setEmail(userInputDto.getEmail());
        user.setUsername(userInputDto.getUsername());
        user.setAvatar(userInputDto.getAvatar());
        return modelMapper.map(userRepository.save(user), UserOutputDto.class);
    }
    @Override
    public UserOutputDto updatePassword(Long id, UserInputDto userInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted", id));
        }
        Map<String, String> errors = validatorUtil.validate(userInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        user.setPassword(bCryptPasswordEncoder.encode(userInputDto.getPassword()));
        return modelMapper.map(userRepository.save(user), UserOutputDto.class);
    }
    @Override
    public void blockOrUnblock(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted", id));
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getRole().getAuthority().equals("ROLE_MODERATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not authorized to perform this action");
        }
        user.setBlocked(!user.isBlocked());
        try {
            userBlockedOrUnblockedEmailUtil.sendEmail(user.getEmail(), user);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
        userRepository.save(user);
    }
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d has been deleted already", id));
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getRole().getAuthority().equals("ROLE_USER") && !currentUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not authorized to perform this action");
        }
        user.setDeleted(true);
        user.setNotifications(false);
        try {
            userDeletedEmailUtil.sendEmail(user.getEmail(), user);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
        userRepository.save(user);
    }
}
