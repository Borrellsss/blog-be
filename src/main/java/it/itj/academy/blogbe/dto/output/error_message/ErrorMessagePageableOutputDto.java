package it.itj.academy.blogbe.dto.output.error_message;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorMessagePageableOutputDto {
    private List<ErrorMessageOutputDto> errorMessages;
    private int totalPages;
    private long totalElements;
}
