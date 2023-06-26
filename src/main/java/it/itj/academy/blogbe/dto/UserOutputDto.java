package it.itj.academy.blogbe.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserOutputDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Byte age;
    private String email;
    private String username;
    private String avatar;
    private List<RoleOutputDto> roles;
}
