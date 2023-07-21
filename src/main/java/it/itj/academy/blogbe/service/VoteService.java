package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.VoteInputDto;
import it.itj.academy.blogbe.dto.output.vote.VoteOutputDto;

public interface VoteService {
    VoteOutputDto create(VoteInputDto voteInputDto);
    Long countPostLikes(Long postId, Boolean liked);
    VoteOutputDto update(VoteInputDto voteInputDto);
    void delete(VoteInputDto voteInputDto);
}
