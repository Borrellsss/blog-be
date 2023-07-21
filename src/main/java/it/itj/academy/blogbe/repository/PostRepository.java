package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    Optional<Post> findByTitle(String title);
    Page<Post> findByTitleContaining(String title, Pageable pageable);
    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Post> findByCategoryName(String categoryName, Pageable pageable);
    Page<Post> findByTagsId(Long tagId, Pageable pageable);
    Page<Post> findByTagsName(String tagName, Pageable pageable);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    Page<Post> findByUserUsername(String username, Pageable pageable);
    Page<Post> findByValidIsFalse(Pageable pageable);
    boolean existsByTitle(String title);
}
