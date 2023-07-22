package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.CommentInputDto;
import it.itj.academy.blogbe.dto.output.post.CommentOutputDto;
import it.itj.academy.blogbe.dto.output.post.CommentPageableOutputDto;
import it.itj.academy.blogbe.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/comments")
public class CommentController {
    private final CommentService commentService;
    private final int PAGE_SIZE = 20;

    // INSERT
    @PostMapping
    public ResponseEntity<CommentOutputDto> create(@RequestBody CommentInputDto commentInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(commentService.create(commentInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping(value = "post/{postId}")
    public ResponseEntity<CommentPageableOutputDto> readByPostIdOrderByCreatedAtDesc(@PathVariable Long postId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(commentService.readByPostIdOrderByCreatedAtDesc(postId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentOutputDto> update(@PathVariable Long id, @RequestBody CommentInputDto commentInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(commentService.update(id, commentInputDto), HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
