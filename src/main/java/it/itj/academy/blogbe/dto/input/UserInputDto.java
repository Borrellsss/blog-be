package it.itj.academy.blogbe.dto.input;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserInputDto {
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private boolean notifications;
    private Long role;
}
