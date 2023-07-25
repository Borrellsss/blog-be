package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.CommentInputDto;
import it.itj.academy.blogbe.dto.output.post.CommentOutputDto;
import it.itj.academy.blogbe.dto.output.post.CommentPageableOutputDto;
import it.itj.academy.blogbe.entity.Comment;
import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.repository.CommentRepository;
import it.itj.academy.blogbe.repository.PostRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import it.itj.academy.blogbe.service.CommentService;
import it.itj.academy.blogbe.util.PageableUtil;
import it.itj.academy.blogbe.util.ValidatorUtil;
import it.itj.academy.blogbe.util.email.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;
    private final EmailUtil<Comment> commentEmailUtil;

    @Override
    public CommentOutputDto create(CommentInputDto commentInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, String> errors = validatorUtil.validate(commentInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        if (!userRepository.existsById(commentInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!postRepository.existsById(commentInputDto.getPost())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!loggedUser.getId().equals(commentInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't comment for another user");
        }
        Comment comment = modelMapper.map(commentInputDto, Comment.class);
        comment.setUser(userRepository.findById(commentInputDto.getUser()).get());
        comment.setPost(postRepository.findById(commentInputDto.getPost()).get());
        CommentOutputDto commentOutputDto = modelMapper.map(commentRepository.save(comment), CommentOutputDto.class);
        if (comment.getPost().getUser().isNotifications() && !comment.getPost().getUser().getId().equals(comment.getUser().getId())) {
            try {
                commentEmailUtil.sendEmail(comment.getPost().getUser().getEmail(), comment);
            } catch (Exception e) {
                log.error("Error sending email: {}", e.getMessage());
            }
        }
        return commentOutputDto;
    }
    @Override
    public CommentPageableOutputDto readAllByUserIdOrderByCreatedAtDesc(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        return pageableUtil.commentPageableOutputDto(comments);
    }
    @Override
    public CommentPageableOutputDto readAllByPostIdOrderByCreatedAtDesc(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageable);
        return pageableUtil.commentPageableOutputDto(comments);
    }
    @Override
    public Long countCommentByPostId(Long postId) {
        return commentRepository.countCommentByPostId(postId);
    }
    @Override
    public CommentOutputDto update(Long id, CommentInputDto commentInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        Map<String, String> errors = validatorUtil.validate(commentInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        if (!userRepository.existsById(commentInputDto.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!postRepository.existsById(commentInputDto.getPost())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!loggedUser.getId().equals(comment.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't update another user's comment");
        }
        comment.setContent(commentInputDto.getContent());
        return modelMapper.map(commentRepository.save(comment), CommentOutputDto.class);
    }
    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!loggedUser.getId().equals(comment.getUser().getId()) && !loggedUser.getRole().getAuthority().equals("ROLE_MODERATOR")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete another user's comment");
        }
        commentRepository.delete(comment);
    }
}
