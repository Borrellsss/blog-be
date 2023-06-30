package it.itj.academy.blogbe.dto.output.post;

import it.itj.academy.blogbe.dto.output.user.UserOutputDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostOutputDto {
    private Long id;
    private String title;
    private String content;
    private boolean valid;
    private Long verifiedBy;
    private LocalDate verifiedAt;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private UserOutputDto user;
    private CategoryOutputDto category;
    private List<TagOutputDto> tags;
    private List<VoteOutputDto> votes;
}
