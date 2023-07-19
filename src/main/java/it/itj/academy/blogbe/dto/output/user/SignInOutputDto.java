package it.itj.academy.blogbe.dto.output.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignInOutputDto {
    private String jwt;
}
