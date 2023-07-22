package it.itj.academy.blogbe.dto.output.comment;

import it.itj.academy.blogbe.dto.output.category.TagOutputDto;
import it.itj.academy.blogbe.dto.output.tag.CategoryOutputDto;
import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
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
    private UserOutputDto user;
    private CategoryOutputDto category;
    private List<TagOutputDto> tags;
}
