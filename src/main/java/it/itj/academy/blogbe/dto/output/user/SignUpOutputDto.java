package it.itj.academy.blogbe.dto.output.user;

import it.itj.academy.blogbe.dto.output.role.RoleOutputDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignUpOutputDto {
    private Long id;
    private String username;
    private RoleOutputDto role;
}
