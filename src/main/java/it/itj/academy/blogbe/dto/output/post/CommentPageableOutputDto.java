package it.itj.academy.blogbe.dto.output.post;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentPageableOutputDto {
    private List<CommentOutputDto> comments;
    private int totalPages;
    private long totalElements;
}
