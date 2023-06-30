package it.itj.academy.blogbe.dto.input;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserInputDto {
    private String firstName;
    private String lastName;
    private Byte age;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private Set<Long> roles;
}