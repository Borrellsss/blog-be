package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.CommentInputDto;
import it.itj.academy.blogbe.dto.output.post.CommentOutputDto;
import it.itj.academy.blogbe.dto.output.post.CommentPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface CommentService {
    CommentOutputDto create(CommentInputDto commentInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    CommentPageableOutputDto readAllByUserIdOrderByCreatedAtDesc(Long userId, int page, int size);
    CommentPageableOutputDto readAllByPostIdOrderByCreatedAtDesc(Long postId, int page, int size);
    Long countCommentByPostId(Long postId);
    CommentOutputDto update(Long id, CommentInputDto commentInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void delete(Long id);
}
