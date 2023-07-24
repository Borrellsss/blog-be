package it.itj.academy.blogbe.dto.output.tag;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TagOutputDto {
    private Long id;
    private String name;
    private String description;
    private Long createdBy;
    private Long updatedBy;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private List<CategoryOutputDto> categories;
}
