package it.itj.academy.blogbe.dto.input;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TagInputDto {
    private String name;
    private Set<Long> categories;
}
