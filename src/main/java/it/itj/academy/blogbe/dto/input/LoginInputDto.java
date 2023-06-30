package it.itj.academy.blogbe.dto.input;

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
