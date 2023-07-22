package it.itj.academy.blogbe.dto.output.post;

import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentOutputDto {
    private Long id;
    private String content;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private UserOutputDto user;
}
