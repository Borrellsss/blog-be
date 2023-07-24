package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;
import it.itj.academy.blogbe.service.PostService;
import jakarta.mail.MessagingException;
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
    public ResponseEntity<PostOutputDto> create(@RequestBody PostInputDto postInputDto) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return new ResponseEntity<>(postService.create(postInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping(value = "/most-popular")
    public ResponseEntity<PostPageableOutputDto> readAllByValidIsTrueOrderByCommentsDesc(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByValidIsTrueOrderByCommentsDesc(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/most-up-voted")
    public ResponseEntity<PostPageableOutputDto> readAllByVotesIsTrueAndValidIsTrueOrderByVotesDesc(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByVotesIsTrueAndValidIsTrueOrderByVotesDesc(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/state")
    public ResponseEntity<PostPageableOutputDto> readAllByValidOrderByCreatedAtDesc(@RequestParam String valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByValidOrderByCreatedAtDesc(valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByTitleContainingAndValidIsTrueOrderByCreatedAtDesc(@RequestParam String value, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByTitleContainingAndValidIsTrueOrderByCreatedAtDesc(value, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category/{categoryId}")
    public ResponseEntity<PostPageableOutputDto> readAllByCategoryIdAndValidIsTrueOrderByCreatedAtDesc(@PathVariable Long categoryId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByCategoryIdAndValidIsTrueOrderByCreatedAtDesc(categoryId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category")
    public ResponseEntity<PostPageableOutputDto> readAllByCategoryNameAndValidIsTrueOrderByCreatedAtDesc(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByCategoryNameAndValidIsTrueOrderByCreatedAtDesc(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category/{categoryName}/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByCategoryNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(@PathVariable String categoryName, @RequestParam String value, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByCategoryNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(categoryName, value, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag/{tagId}")
    public ResponseEntity<PostPageableOutputDto> readAllByTagsIdAndValidIsTrueOrderByCreatedAtDesc(@PathVariable Long tagId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByTagsIdAndValidIsTrueOrderByCreatedAtDesc(tagId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag")
    public ResponseEntity<PostPageableOutputDto> readAllByTagsNameAndValidIsTrueOrderByCreatedAtDesc(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByTagsNameAndValidIsTrueOrderByCreatedAtDesc(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag/{tagName}/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByTagsNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(@PathVariable String tagName, @RequestParam String value, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByTagsNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(tagName, value, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto> readAllByUserIdAndValidOrderByCreatedAtDesc(@PathVariable Long userId, @RequestParam String valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByUserIdAndValidOrderByCreatedAtDesc(userId, valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user")
    public ResponseEntity<it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto> readAllByUserUsernameAndValidOrderByCreatedAtDesc(@RequestParam String username, @RequestParam Boolean valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByUserUsernameAndValidOrderByCreatedAtDesc(username, valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user/{username}/title-contains")
    public ResponseEntity<PostPageableOutputDto> readAllByUserUsernameAndTitleContainingAndValidOrderByCreatedAtDesc(@PathVariable String username, @RequestParam String value, @RequestParam Boolean valid, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByUserUsernameAndTitleContainingAndValidOrderByCreatedAtDesc(username, value, valid, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostOutputDto> readById(@PathVariable Long id) {
        return new ResponseEntity<>(postService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "title/{title}")
    public ResponseEntity<PostOutputDto> readByTitleAndValidIsTrue(@PathVariable String title) {
        return new ResponseEntity<>(postService.readByTitleAndValidIsTrue(title), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<PostOutputDto> update(@PathVariable Long id, @RequestBody PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.update(id, postInputDto), HttpStatus.OK);
    }
    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, MessagingException {
        postService.acceptOrReject(id, true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, MessagingException {
        postService.acceptOrReject(id, false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // DELETE
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws MessagingException {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
