package it.itj.academy.blogbe.dto.input;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ValidationInputDto {
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
}
