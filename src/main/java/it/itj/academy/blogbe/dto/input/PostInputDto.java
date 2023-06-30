package it.itj.academy.blogbe.dto.input;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostInputDto {
    private String title;
    private String content;
    private Boolean valid;
    private Long category;
    private Set<Long> tags;
}
