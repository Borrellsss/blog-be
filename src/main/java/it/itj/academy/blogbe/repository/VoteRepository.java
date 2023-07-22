package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Vote.VotePk> {
    Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);
    Long countByPostIdAndLiked(Long postId, Boolean liked);
}
