package it.itj.academy.blogbe.util.db_filler;

import it.itj.academy.blogbe.entity.Validation;
import it.itj.academy.blogbe.repository.UserRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class ValidationFiller {
    private final ValidationRepository validationRepository;
    private final UserRepository userRepository;

    public void fillValidations() {
        validationRepository.deleteAll(validationRepository.findAll());
        Set<Validation> validations = Set.of(
            // SignUpInputDto
            new Validation("SUDFN001", "SignUpInputDto.firstName", true, true, 2, 50, null, null, null, null, null),
            new Validation("SUDLN001", "SignUpInputDto.lastName", true, true, 2, 50, null, null, null, null, null),
            new Validation("SUDBD001", "SignUpInputDto.birthdate", true, true, 18, null, null, null, null, null, null),
            new Validation("SUDEM001", "SignUpInputDto.email", true, true, null, null, "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", null, null, null, null),
            new Validation("SUDUN001", "SignUpInputDto.username", true, true, 2, 50, null, null, null, null, null),
            new Validation("SUDPW001", "SignUpInputDto.password", true, true, 8, null, null, (byte) 1, (byte) 1, (byte) 1, (byte) 1),
            new Validation("SUDAV001", "SignUpInputDto.avatar", false, false, null, null, "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", null, null, null, null),
            // SignInInputDto
            new Validation("SIDUN001", "SignInInputDto.username", true, true, 2, 50, null, null, null, null, null),
            new Validation("SIDPW001", "SignInInputDto.password", true, true, 8, null, null, (byte) 1, (byte) 1, (byte) 1, (byte) 1),
            // UserInputDto
            new Validation("USDFN001", "UserInputDto.firstName", true, true, 2, 50, null, null, null, null, null),
            new Validation("USDLN001", "UserInputDto.lastName", true, true, 2, 50, null, null, null, null, null),
            new Validation("USDBD001", "UserInputDto.birthdate", true, true, 18, null, null, null, null, null, null),
            new Validation("USDEM001", "UserInputDto.email", true, true, null, null, "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", null, null, null, null),
            new Validation("USDUN001", "UserInputDto.username", true, true, 2, 50, null, null, null, null, null),
            new Validation("USDPW001", "UserInputDto.password", true, true, 8, null, null, (byte) 1, (byte) 1, (byte) 1, (byte) 1),
            new Validation("USDAV001", "UserInputDto.avatar", false, false, null, null, "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", null, null, null, null)
        );
        validations.forEach(category -> {
            category.setCreatedBy(userRepository.findByUsername("e.borrello").get().getId());
            category.setUpdatedBy(userRepository.findByUsername("e.borrello").get().getId());
        });
        validationRepository.saveAll(validations);
    }
}
