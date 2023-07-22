package it.itj.academy.blogbe.controller;

import it.itj.academy.blogbe.dto.input.VoteInputDto;
import it.itj.academy.blogbe.dto.output.vote.VoteOutputDto;
import it.itj.academy.blogbe.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/votes")
public class VoteController {
    private final VoteService voteService;

    // INSERT
    @PostMapping
    public ResponseEntity<VoteOutputDto> create(@RequestBody VoteInputDto voteInputDto) {
        return new ResponseEntity<>(voteService.create(voteInputDto), HttpStatus.CREATED);
    }
    // SELECT
    @GetMapping(value = "/{user}/{post}")
    public ResponseEntity<VoteOutputDto> readByUserIdAndPostId(@PathVariable Long user, @PathVariable Long post) {
        return new ResponseEntity<>(voteService.readByUserIdAndPostId(user, post), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Long> countPostLikes(@RequestParam Long post, @RequestParam Boolean liked) {
        return new ResponseEntity<>(voteService.countPostLikes(post, liked), HttpStatus.OK);
    }
    // UPDATE
    @PutMapping
    public ResponseEntity<VoteOutputDto> update(@RequestBody VoteInputDto voteInputDto) {
        return new ResponseEntity<>(voteService.update(voteInputDto), HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody VoteInputDto voteInputDto) {
        voteService.delete(voteInputDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
