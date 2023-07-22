package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.VoteInputDto;
import it.itj.academy.blogbe.dto.output.vote.VoteOutputDto;
import it.itj.academy.blogbe.entity.Post;
import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.entity.Vote;
import it.itj.academy.blogbe.repository.PostRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import it.itj.academy.blogbe.repository.VoteRepository;
import it.itj.academy.blogbe.service.VoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public VoteOutputDto create(VoteInputDto voteInputDto) {
        if (voteRepository.existsById(new Vote.VotePk(voteInputDto.getUser(), voteInputDto.getPost()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vote already exists");
        }
        if (!userRepository.existsById(voteInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!loggedUser.getId().equals(voteInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't vote for another user");
        }
        Post post = postRepository.findById(voteInputDto.getPost())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        Vote vote = new Vote();
        vote.setUser(loggedUser);
        vote.setPost(post);
        vote.setLiked(voteInputDto.getLiked());
        return modelMapper.map(voteRepository.save(vote), VoteOutputDto.class);
    }
    @Override
    public VoteOutputDto readByUserIdAndPostId(Long userId, Long postId) {
        Vote vote = voteRepository.findByUserIdAndPostId(userId, postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found"));
        return modelMapper.map(vote, VoteOutputDto.class);
    }
    @Override
    public Long countPostLikes(Long postId, Boolean liked) {
        return voteRepository.countByPostIdAndLiked(postId, liked);
    }
    @Override
    public VoteOutputDto update(VoteInputDto voteInputDto) {
        Vote vote = voteRepository.findById(new Vote.VotePk(voteInputDto.getUser(), voteInputDto.getPost()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found"));
        if (!userRepository.existsById(voteInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!loggedUser.getId().equals(voteInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't vote for another user");
        }
        Post post = postRepository.findById(voteInputDto.getPost())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        vote.setLiked(voteInputDto.getLiked());
        return modelMapper.map(voteRepository.save(vote), VoteOutputDto.class);
    }
    @Override
    public void delete(VoteInputDto voteInputDto) {
        Vote vote = voteRepository.findById(new Vote.VotePk(voteInputDto.getUser(), voteInputDto.getPost()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found"));
        if (!userRepository.existsById(voteInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!loggedUser.getId().equals(voteInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't vote for another user");
        }
        voteRepository.delete(vote);
    }
}
