package it.itj.academy.blogbe.dto.output.error_message;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorMessageOutputDto {
    private Long id;
    private String errorType;
    private String message;
    private String validationCode;
    private Long createdBy;
    private Long updatedBy;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private List<ValidationOutputDto> validations;
}
