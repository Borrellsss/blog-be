package it.itj.academy.blogbe.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterOutputDto {
    private Long id;
    private String username;
    private List<RoleOutputDto> roles;
}
