package it.itj.academy.blogbe.dto.output.user;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostPageableOutputDto {
    private List<PostOutputDto> posts;
    private int totalPages;
    private long totalElements;
}
