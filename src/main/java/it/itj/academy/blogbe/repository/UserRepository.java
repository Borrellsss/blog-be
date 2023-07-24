package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByDeletedIsFalseOrderByUsername(Pageable pageable);
    Page<User> findAllByUsernameContainingAndDeletedIsFalseOrderByUsername(String username, Pageable pageable);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
