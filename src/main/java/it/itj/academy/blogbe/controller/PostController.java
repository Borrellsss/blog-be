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
    public ResponseEntity<PostOutputDto> create(@RequestBody PostInputDto postInputDto) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return new ResponseEntity<>(postService.create(postInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping
    public ResponseEntity<PostPageableOutputDto> findAllByOrderByUpdatedAtDesc(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readAllByOrderByUpdatedAtDesc(page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostOutputDto> readById(@PathVariable Long id) {
        return new ResponseEntity<>(postService.readById(id), HttpStatus.OK);
    }
    @GetMapping(value = "title/{title}")
    public ResponseEntity<PostOutputDto> readByTitle(@PathVariable String title) {
        return new ResponseEntity<>(postService.readByTitle(title), HttpStatus.OK);
    }
    @GetMapping(value = "/title-contains/{title}")
    public ResponseEntity<PostPageableOutputDto> readByTitleContaining(@PathVariable String title, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByTitleContaining(title, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category/{categoryId}")
    public ResponseEntity<PostPageableOutputDto> readByCategoryId(@PathVariable Long categoryId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByCategoryId(categoryId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/category")
    public ResponseEntity<PostPageableOutputDto> readByCategoryName(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByCategoryName(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag/{tagId}")
    public ResponseEntity<PostPageableOutputDto> readByTagId(@PathVariable Long tagId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByTagId(tagId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/tag")
    public ResponseEntity<PostPageableOutputDto> readByTagName(@RequestParam String name, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByTagName(name, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<PostPageableOutputDto> readByUserId(@PathVariable Long userId, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByUserId(userId, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/user")
    public ResponseEntity<PostPageableOutputDto> readByUserUsername(@RequestParam String username, @RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByUserUsername(username, page, pageable.getPageSize()), HttpStatus.OK);
    }
    @GetMapping(value = "/pending")
    public ResponseEntity<PostPageableOutputDto> readByValidIsFalse(@RequestParam int page, @PageableDefault(size = PAGE_SIZE) Pageable pageable) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.readByValidIsFalse(page, pageable.getPageSize()), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<PostOutputDto> update(@PathVariable Long id, @RequestBody PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(postService.update(id, postInputDto), HttpStatus.OK);
    }
    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        postService.acceptOrReject(id, true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
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
