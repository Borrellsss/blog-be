package it.itj.academy.blogbe.dto.output;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TagPageableOutputDto {
    private List<TagOutputDto> tags;
    private int totalPages;
    private long totalElements;
}
