package it.itj.academy.blogbe.dto.input;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignUpInputDto {
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private Long role;
}
