package it.itj.academy.blogbe.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterInputDto {
    private String firstName;
    private String lastName;
    private Byte age;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private Set<Long> roles;
}
