package it.itj.academy.blogbe.dto.output.vote;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VoteOutputDto {
    private UserOutputDto user;
    private PostOutputDto post;
    private Boolean liked;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
}
