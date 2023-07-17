package it.itj.academy.blogbe.dto.input;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignInInputDto {
    private String username;
    private String password;
}
