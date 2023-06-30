package it.itj.academy.blogbe.dto.output;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginOutputDto {
    private String jwt;
    private UserOutputDto user;
}
