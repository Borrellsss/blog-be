package it.itj.academy.blogbe.util.db_filler;

import it.itj.academy.blogbe.entity.ErrorMessage;
import it.itj.academy.blogbe.repository.ErrorMessageRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class ErrorMessageFiller {
    private final ErrorMessageRepository errorMessageRepository;
    private final ValidationRepository validationRepository;
    private final UserRepository userRepository;

    public void fillErrorMessages() {
        errorMessageRepository.deleteAll(errorMessageRepository.findAll());
        Set<ErrorMessage> errorMessages = Set.of(
            // SignUpInputDto
            // firstName
            new ErrorMessage("SignUpInputDto.firstName.notNull", "First name is required", validationRepository.findByCode("SUDFN001").get()),
            new ErrorMessage("SignUpInputDto.firstName.notEmpty", "First name cannot be empty", validationRepository.findByCode("SUDFN001").get()),
            new ErrorMessage("SignUpInputDto.firstName.min", "First name must be at least 2 characters long", validationRepository.findByCode("SUDFN001").get()),
            new ErrorMessage("SignUpInputDto.firstName.max", "First name cannot be longer than 50 characters", validationRepository.findByCode("SUDFN001").get()),
            // lastName
            new ErrorMessage("SignUpInputDto.lastName.notNull", "Last name is required", validationRepository.findByCode("SUDLN001").get()),
            new ErrorMessage("SignUpInputDto.lastName.notEmpty", "Last name cannot be empty", validationRepository.findByCode("SUDLN001").get()),
            new ErrorMessage("SignUpInputDto.lastName.min", "Last name must be at least 2 characters long", validationRepository.findByCode("SUDLN001").get()),
            new ErrorMessage("SignUpInputDto.lastName.max", "Last name cannot be longer than 50 characters", validationRepository.findByCode("SUDLN001").get()),
            // birthdate
            new ErrorMessage("SignUpInputDto.birthdate.notNull", "Birthdate is required", validationRepository.findByCode("SUDBD001").get()),
            new ErrorMessage("SignUpInputDto.birthdate.notEmpty", "Birthdate cannot be empty", validationRepository.findByCode("SUDBD001").get()),
            new ErrorMessage("SignUpInputDto.birthdate.min", "You must be an adult for signing up", validationRepository.findByCode("SUDBD001").get()),
            // email
            new ErrorMessage("SignUpInputDto.email.notNull", "Email is required", validationRepository.findByCode("SUDEM001").get()),
            new ErrorMessage("SignUpInputDto.email.notEmpty", "Email cannot be empty", validationRepository.findByCode("SUDEM001").get()),
            new ErrorMessage("SignUpInputDto.email.regex", "Email must be a valid email address", validationRepository.findByCode("SUDEM001").get()),
            // username
            new ErrorMessage("SignUpInputDto.username.notNull", "Username is required", validationRepository.findByCode("SUDUN001").get()),
            new ErrorMessage("SignUpInputDto.username.notEmpty", "Username cannot be empty", validationRepository.findByCode("SUDUN001").get()),
            new ErrorMessage("SignUpInputDto.username.min", "Username must be at least 2 characters long", validationRepository.findByCode("SUDUN001").get()),
            new ErrorMessage("SignUpInputDto.username.max", "Username cannot be longer than 50 characters", validationRepository.findByCode("SUDUN001").get()),
            // password
            new ErrorMessage("SignUpInputDto.password.notNull", "Password is required", validationRepository.findByCode("SUDPW001").get()),
            new ErrorMessage("SignUpInputDto.password.notEmpty", "Password cannot be empty", validationRepository.findByCode("SUDPW001").get()),
            new ErrorMessage("SignUpInputDto.password.min", "Password must be at least 8 characters long", validationRepository.findByCode("SUDPW001").get()),
            new ErrorMessage("SignUpInputDto.password.minUpperCaseLetters", "Password must contain at least 1 uppercase letter", validationRepository.findByCode("SUDPW001").get()),
            new ErrorMessage("SignUpInputDto.password.minLowerCaseLetters", "Password must contain at least 1 lowercase letter", validationRepository.findByCode("SUDPW001").get()),
            new ErrorMessage("SignUpInputDto.password.minDigits", "Password must contain at least 1 number", validationRepository.findByCode("SUDPW001").get()),
            new ErrorMessage("SignUpInputDto.password.minSpecialCharacters", "Password must contain at least 1 special character", validationRepository.findByCode("SUDPW001").get()),
            // avatar
            new ErrorMessage("SignUpInputDto.avatar.regex", "Avatar must be a valid URL", validationRepository.findByCode("SUDAV001").get()),
            // SignInInputDto
            // username
            new ErrorMessage("SignInInputDto.username.notNull", "Username is required", validationRepository.findByCode("SIDUN001").get()),
            new ErrorMessage("SignInInputDto.username.notEmpty", "Username cannot be empty", validationRepository.findByCode("SIDUN001").get()),
            new ErrorMessage("SignInInputDto.username.min", "Username must be at least 2 characters long", validationRepository.findByCode("SIDUN001").get()),
            new ErrorMessage("SignInInputDto.username.max", "Username cannot be longer than 50 characters", validationRepository.findByCode("SIDUN001").get()),
            // password
            new ErrorMessage("SignInInputDto.password.notNull", "Password is required", validationRepository.findByCode("SIDPW001").get()),
            new ErrorMessage("SignInInputDto.password.notEmpty", "Password cannot be empty", validationRepository.findByCode("SIDPW001").get()),
            new ErrorMessage("SignInInputDto.password.min", "Password must be at least 8 characters long", validationRepository.findByCode("SIDPW001").get()),
            new ErrorMessage("SignInInputDto.password.minUpperCaseLetters", "Password must contain at least 1 uppercase letter", validationRepository.findByCode("SIDPW001").get()),
            new ErrorMessage("SignInInputDto.password.minLowerCaseLetters", "Password must contain at least 1 lowercase letter", validationRepository.findByCode("SIDPW001").get()),
            new ErrorMessage("SignInInputDto.password.minDigits", "Password must contain at least 1 number", validationRepository.findByCode("SIDPW001").get()),
            new ErrorMessage("SignInInputDto.password.minSpecialCharacters", "Password must contain at least 1 special character", validationRepository.findByCode("SIDPW001").get()),
            // UserInputDto
            // firstName
            new ErrorMessage("UserInputDto.firstName.notNull", "First name is required", validationRepository.findByCode("USDFN001").get()),
            new ErrorMessage("UserInputDto.firstName.notEmpty", "First name cannot be empty", validationRepository.findByCode("USDFN001").get()),
            new ErrorMessage("UserInputDto.firstName.min", "First name must be at least 2 characters long", validationRepository.findByCode("USDFN001").get()),
            new ErrorMessage("UserInputDto.firstName.max", "First name cannot be longer than 50 characters", validationRepository.findByCode("USDFN001").get()),
            // lastName
            new ErrorMessage("UserInputDto.lastName.notNull", "Last name is required", validationRepository.findByCode("USDLN001").get()),
            new ErrorMessage("UserInputDto.lastName.notEmpty", "Last name cannot be empty", validationRepository.findByCode("USDLN001").get()),
            new ErrorMessage("UserInputDto.lastName.min", "Last name must be at least 2 characters long", validationRepository.findByCode("USDLN001").get()),
            new ErrorMessage("UserInputDto.lastName.max", "Last name cannot be longer than 50 characters", validationRepository.findByCode("USDLN001").get()),
            // birthdate
            new ErrorMessage("UserInputDto.birthdate.notNull", "Birthdate is required", validationRepository.findByCode("USDBD001").get()),
            new ErrorMessage("UserInputDto.birthdate.notEmpty", "Birthdate cannot be empty", validationRepository.findByCode("USDBD001").get()),
            new ErrorMessage("UserInputDto.birthdate.min", "You must be an adult", validationRepository.findByCode("USDBD001").get()),
            // email
            new ErrorMessage("UserInputDto.email.notNull", "Email is required", validationRepository.findByCode("USDEM001").get()),
            new ErrorMessage("UserInputDto.email.notEmpty", "Email cannot be empty", validationRepository.findByCode("USDEM001").get()),
            new ErrorMessage("UserInputDto.email.regex", "Email must be a valid email address", validationRepository.findByCode("USDEM001").get()),
            // username
            new ErrorMessage("UserInputDto.username.notNull", "Username is required", validationRepository.findByCode("USDUN001").get()),
            new ErrorMessage("UserInputDto.username.notEmpty", "Username cannot be empty", validationRepository.findByCode("USDUN001").get()),
            new ErrorMessage("UserInputDto.username.min", "Username must be at least 2 characters long", validationRepository.findByCode("USDUN001").get()),
            new ErrorMessage("UserInputDto.username.max", "Username cannot be longer than 50 characters", validationRepository.findByCode("USDUN001").get()),
            // password
            new ErrorMessage("UserInputDto.password.notNull", "Password is required", validationRepository.findByCode("USDPW001").get()),
            new ErrorMessage("UserInputDto.password.notEmpty", "Password cannot be empty", validationRepository.findByCode("USDPW001").get()),
            new ErrorMessage("UserInputDto.password.min", "Password must be at least 8 characters long", validationRepository.findByCode("USDPW001").get()),
            new ErrorMessage("UserInputDto.password.minUpperCaseLetters", "Password must contain at least 1 uppercase letter", validationRepository.findByCode("USDPW001").get()),
            new ErrorMessage("UserInputDto.password.minLowerCaseLetters", "Password must contain at least 1 lowercase letter", validationRepository.findByCode("USDPW001").get()),
            new ErrorMessage("UserInputDto.password.minDigits", "Password must contain at least 1 number", validationRepository.findByCode("USDPW001").get()),
            new ErrorMessage("UserInputDto.password.minSpecialCharacters", "Password must contain at least 1 special character", validationRepository.findByCode("USDPW001").get()),
            // avatar
            new ErrorMessage("UserInputDto.avatar.regex", "Avatar must be a valid URL", validationRepository.findByCode("USDAV001").get())
        );
        errorMessages.forEach(errorMessage -> {
            errorMessage.setCreatedBy(userRepository.findByUsername("m.fabozzi").get().getId());
            errorMessage.setUpdatedBy(userRepository.findByUsername("m.fabozzi").get().getId());
        });
        errorMessageRepository.saveAll(errorMessages);
    }

}
