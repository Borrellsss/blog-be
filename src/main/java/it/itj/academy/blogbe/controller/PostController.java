package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;
import it.itj.academy.blogbe.service.PostService;
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
@RequestMapping(value = "/posts")
public class PostController {
    private final PostService postService;
    private final int PAGE_SIZE = 20;

    // INSERT
    @PostMapping
    public ResponseEntity<PostOutputDto> create(@RequestBody PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.create(postInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping(value = "/most-popular")
    public ResponseEntity<PostPageableOutputDto> readAllByValidIsTrueAndUserDeletedIsFalseOrderByCommentsDesc(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByValidIsTrueAndUserDeletedIsFalseOrderByCommentsDesc(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/most-up-voted")
    public ResponseEntity<PostPageableOutputDto> readAllByVotesIsTrueAndValidIsTrueAndUserDeletedIsFalseOrderByVotesDesc(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByVotesIsTrueAndValidIsTrueAndUserDeletedIsFalseOrderByVotesDesc(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/state")
    public ResponseEntity<PostPageableOutputDto> readAllByValidAndUserDeletedIsFalseOrderByCreatedAtDesc(@RequestParam String valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByValidAndUserDeletedIsFalseOrderByCreatedAtDesc(valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(@RequestParam String value, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(value, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category/{categoryId}")
    public ResponseEntity<PostPageableOutputDto> readAllByCategoryIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(@PathVariable Long categoryId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByCategoryIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(categoryId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category")
    public ResponseEntity<PostPageableOutputDto> readAllByCategoryNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByCategoryNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category/{categoryName}/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByCategoryNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(@PathVariable String categoryName, @RequestParam String value, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByCategoryNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(categoryName, value, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag/{tagId}")
    public ResponseEntity<PostPageableOutputDto> readAllByTagsIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(@PathVariable Long tagId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByTagsIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(tagId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag")
    public ResponseEntity<PostPageableOutputDto> readAllByTagsNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByTagsNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag/{tagName}/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByTagsNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(@PathVariable String tagName, @RequestParam String value, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByTagsNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(tagName, value, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto> readAllByUserIdAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(@PathVariable Long userId, @RequestParam String valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByUserIdAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(userId, valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user")
    public ResponseEntity<it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto> readAllByUserUsernameAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(@RequestParam String username, @RequestParam Boolean valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByUserUsernameAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(username, valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user/{username}/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByUserUsernameAndTitleContainingAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(@PathVariable String username, @RequestParam String value, @RequestParam Boolean valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        return new ResponseEntity<>(postService.readAllByUserUsernameAndTitleContainingAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(username, value, valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostOutputDto> readByIdAndUserDeletedIsFalse(@PathVariable Long id) {
        return new ResponseEntity<>(postService.readByIdAndUserDeletedIsFalse(id), HttpStatus.OK);
    }
    @GetMapping(value = "title/{title}")
    public ResponseEntity<PostOutputDto> readByTitleAndValidIsTrueAndUserDeletedIsFalse(@PathVariable String title) {
        return new ResponseEntity<>(postService.readByTitleAndValidIsTrueAndUserDeletedIsFalse(title), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<PostOutputDto> update(@PathVariable Long id, @RequestBody PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.update(id, postInputDto), HttpStatus.OK);
    }
    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id) {
        postService.acceptOrReject(id, true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        postService.acceptOrReject(id, false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
