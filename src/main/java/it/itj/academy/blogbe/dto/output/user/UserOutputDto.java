package it.itj.academy.blogbe.dto.output.user;

import it.itj.academy.blogbe.dto.output.role.RoleOutputDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private boolean blocked = false;
    private boolean deleted = false;
    private Long createdBy;
    private Long updatedBy;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private List<RoleOutputDto> roles;
}
