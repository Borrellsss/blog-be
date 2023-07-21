package it.itj.academy.blogbe.dto.output.vote;

import it.itj.academy.blogbe.dto.output.role.RoleOutputDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserOutputDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String username;
    private String avatar;
    private Boolean blocked = false;
    private Boolean deleted = false;
    private Long createdBy;
    private Long updatedBy;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private RoleOutputDto role;
}