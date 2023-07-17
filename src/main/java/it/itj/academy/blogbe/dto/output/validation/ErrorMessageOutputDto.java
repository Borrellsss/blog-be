package it.itj.academy.blogbe.dto.output.validation;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
