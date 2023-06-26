package it.itj.academy.blogbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorMessageInputDto {
    private String errorType;
    private String message;
    private String validation;
}
