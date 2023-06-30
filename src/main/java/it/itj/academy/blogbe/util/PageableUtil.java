package it.itj.academy.blogbe.util;

import it.itj.academy.blogbe.dto.output.*;
import it.itj.academy.blogbe.entity.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
public class PageableUtil {
    private final ModelMapper modelMapper;
    public UserPageableOutputDto userPageableOutputDto(Page<User> users) {
        if (users.hasContent()) {
            UserPageableOutputDto userPageableOutputDto = new UserPageableOutputDto();
            userPageableOutputDto.setUsers(users.stream()
                .filter(user -> !user.isDeleted())
                .map(user -> modelMapper.map(user, UserOutputDto.class))
                .toList());
            userPageableOutputDto.setTotalPages(users.getTotalPages());
            userPageableOutputDto.setTotalElements(users.getTotalElements());
            return userPageableOutputDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found");
        }
    }
    public ValidationPageableOutputDto validationPageableOutputDto(Page<Validation> validations) {
        if (validations.hasContent()) {
            ValidationPageableOutputDto validationPageableOutputDto = new ValidationPageableOutputDto();
            validationPageableOutputDto.setValidations(validations.stream()
                .map(validation -> modelMapper.map(validation, ValidationOutputDto.class))
                .toList());
            validationPageableOutputDto.setTotalPages(validations.getTotalPages());
            validationPageableOutputDto.setTotalElements(validations.getTotalElements());
            return validationPageableOutputDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Validations not found");
        }
    }
    public ErrorMessagePageableOutputDto errorMessagePageableOutputDto(Page<ErrorMessage> errorMessages) {
        if (errorMessages.hasContent()) {
            ErrorMessagePageableOutputDto errorMessagePageableOutputDto = new ErrorMessagePageableOutputDto();
            errorMessagePageableOutputDto.setErrorMessages(errorMessages.stream()
                .map(errorMessage -> {
                    ErrorMessageOutputDto errorMessageOutputDto = modelMapper.map(errorMessage, ErrorMessageOutputDto.class);
                    errorMessageOutputDto.setValidationCode(errorMessage.getValidation().getCode());
                    return errorMessageOutputDto;
                })
                .toList());
            errorMessagePageableOutputDto.setTotalPages(errorMessages.getTotalPages());
            errorMessagePageableOutputDto.setTotalElements(errorMessages.getTotalElements());
            return errorMessagePageableOutputDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error messages not found");
        }
    }
    public CategoryPageableOutputDto categoryPageableOutputDto(Page<Category> categories) {
        if (categories.hasContent()) {
            CategoryPageableOutputDto categoryPageableOutputDto = new CategoryPageableOutputDto();
            categoryPageableOutputDto.setCategories(categories.stream()
                .map(category -> modelMapper.map(category, CategoryOutputDto.class))
                .toList());
            categoryPageableOutputDto.setTotalPages(categories.getTotalPages());
            categoryPageableOutputDto.setTotalElements(categories.getTotalElements());
            return categoryPageableOutputDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categories not found");
        }
    }
    public TagPageableOutputDto tagPageableOutputDto(Page<Tag> tags) {
        if (tags.hasContent()) {
            TagPageableOutputDto tagPageableOutputDto = new TagPageableOutputDto();
            tagPageableOutputDto.setTags(tags.stream()
                .map(tag -> modelMapper.map(tag, TagOutputDto.class))
                .toList());
            tagPageableOutputDto.setTotalPages(tags.getTotalPages());
            tagPageableOutputDto.setTotalElements(tags.getTotalElements());
            return tagPageableOutputDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tags not found");
        }
    }
}
