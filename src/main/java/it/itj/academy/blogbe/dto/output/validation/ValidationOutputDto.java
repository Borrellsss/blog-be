package it.itj.academy.blogbe.dto.output.validation;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ValidationOutputDto {
    private Long id;
    private String code;
    private String field;
    private Boolean notNull;
    private Boolean notEmpty;
    private Integer min;
    private Integer max;
    private String regex;
    private Byte minUpperCaseLetters;
    private Byte minLowerCaseLetters;
    private Byte minDigits;
    private Byte minSpecialCharacters;
    private Long createdBy;
    private Long updatedBy;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private List<ErrorMessageOutputDto> errorMessages;
}
