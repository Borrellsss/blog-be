package it.itj.academy.blogbe.dto.output.user;

import it.itj.academy.blogbe.dto.output.post.CategoryOutputDto;
import it.itj.academy.blogbe.dto.output.post.TagOutputDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostOutputDto {
    private Long id;
    private String title;
    private String content;
    private Boolean valid;
    private Long verifiedBy;
    private LocalDate verifiedAt;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private CategoryOutputDto category;
    private List<TagOutputDto> tags;
    private Integer votes;
}
