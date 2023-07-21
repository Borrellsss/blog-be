package it.itj.academy.blogbe.dto.input;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VoteInputDto {
    private Long user;
    private Long post;
    private Boolean liked;
}
