package it.itj.academy.blogbe.dto.output;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserPageableOutputDto {
    private List<UserOutputDto> users;
    private int totalPages;
    private long totalElements;
}
