package it.itj.academy.blogbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorMessageOutputDto {
    private String errorType;
    private String message;
    private String validationCode;
}
