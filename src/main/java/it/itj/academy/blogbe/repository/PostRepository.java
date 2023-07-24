package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            SELECT
                p.*,
                COUNT(*) AS comments_count
            FROM post p
            INNER JOIN comment c
                ON c.post_id = p.id
            WHERE p.valid = true
            GROUP BY p.id
            ORDER BY comments_count DESC;
        """,
        nativeQuery = true
    )
    Page<Post> findAllByValidIsTrueOrderByCommentsDesc(Pageable pageable);
    @Query(value = """
            SELECT
                p.*
            FROM post p
            INNER JOIN (SELECT
                v.post_id,
                COUNT(v.liked) AS count
            FROM vote v
            WHERE v.liked = true
            GROUP BY v.post_id, v.liked) AS post_likes_count
                ON p.id = post_likes_count.post_id
            WHERE p.valid = true
            ORDER BY post_likes_count.count DESC;
        """,
        nativeQuery = true
    )
    Page<Post> findAllByVotesIsTrueAndValidIsTrueOrderByVotesDesc(Pageable pageable);
    Page<Post> findAllByValidOrderByCreatedAtDesc(Boolean valid, Pageable pageable);
    Page<Post> findAllByTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String title, Pageable pageable);
    Page<Post> findAllByCategoryIdAndValidIsTrueOrderByCreatedAtDesc(Long categoryId, Pageable pageable);
    Page<Post> findAllByCategoryNameAndValidIsTrueOrderByCreatedAtDesc(String categoryName, Pageable pageable);
    Page<Post> findAllByCategoryNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String categoryName, String title, Pageable pageable);
    Page<Post> findAllByTagsIdAndValidIsTrueOrderByCreatedAtDesc(Long tagId, Pageable pageable);
    Page<Post> findAllByTagsNameAndValidIsTrueOrderByCreatedAtDesc(String tagName, Pageable pageable);
    Page<Post> findAllByTagsNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String tagName, String title, Pageable pageable);
    Page<Post> findAllByUserIdAndValidOrderByCreatedAtDesc(Long userId, Boolean valid, Pageable pageable);
    Page<Post> findAllByUserUsernameAndValidOrderByCreatedAtDesc(String username, Boolean valid, Pageable pageable);
    Page<Post> findAllByUserUsernameAndTitleContainingAndValidOrderByCreatedAtDesc(String username, String title, Boolean valid, Pageable pageable);
    Optional<Post> findByTitleAndValidIsTrue(String title);
    boolean existsByTitle(String title);
}
