package it.itj.academy.blogbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginInputDto {
    private String username;
    private String password;
}
