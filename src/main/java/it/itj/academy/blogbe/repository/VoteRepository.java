package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Vote.VotePk> {

}
