package it.itj.academy.blogbe.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ValidationInputDto {
    private String code;
    private String field;
    private boolean nullable = true;
    private Integer min;
    private Integer max;
    private String regex;
    private Byte minUpperCaseLetters;
    private Byte minLowerCaseLetters;
    private Byte minDigits;
    private Byte minSpecialCharacters;

    public ValidationInputDto(String code, String field, Integer min, Integer max) {
        this.code = code;
        this.field = field;
        this.min = min;
        this.max = max;
    }
    public ValidationInputDto(String code, String field, boolean nullable, Integer min, Integer max, Byte minUpperCaseLetters, Byte minLowerCaseLetters, Byte minDigits, Byte minSpecialCharacters) {
        this.code = code;
        this.field = field;
        this.nullable = nullable;
        this.min = min;
        this.max = max;
        this.minUpperCaseLetters = minUpperCaseLetters;
        this.minLowerCaseLetters = minLowerCaseLetters;
        this.minDigits = minDigits;
        this.minSpecialCharacters = minSpecialCharacters;
    }
}
