package it.itj.academy.blogbe.dto.input;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentInputDto {
    private String content;
    private Long post;
    private Long user;
}
