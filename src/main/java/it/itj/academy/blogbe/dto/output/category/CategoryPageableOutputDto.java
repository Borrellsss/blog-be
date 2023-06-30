package it.itj.academy.blogbe.dto.output.category;

import it.itj.academy.blogbe.dto.output.category.CategoryOutputDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryPageableOutputDto {
    private List<CategoryOutputDto> categories;
    private int totalPages;
    private long totalElements;
}
