package it.itj.academy.blogbe.dto.output;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ValidationPageableOutputDto {
    private List<ValidationOutputDto> validations;
    private int totalPages;
    private long totalElements;
}
