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
            INNER JOIN user u
                ON p.user_id = u.id
            WHERE p.valid = true
                AND u.deleted = false
            GROUP BY p.id
            ORDER BY comments_count DESC;
        """,
        nativeQuery = true
    )
    Page<Post> findAllByValidIsTrueAndUserDeletedIsFalseOrderByCommentsDesc(Pageable pageable);
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
            INNER JOIN user u
                ON u.id = p.user_id
            WHERE p.valid = true
                AND u.deleted = false
            ORDER BY post_likes_count.count DESC;
        """,
        nativeQuery = true
    )
    Page<Post> findAllByVotesIsTrueAndValidIsTrueAndUserDeletedIsFalseOrderByVotesDesc(Pageable pageable);
    Page<Post> findAllByValidAndUserDeletedIsFalseOrderByCreatedAtDesc(Boolean valid, Pageable pageable);
    Page<Post> findAllByTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String title, Pageable pageable);
    Page<Post> findAllByCategoryIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(Long categoryId, Pageable pageable);
    Page<Post> findAllByCategoryNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String categoryName, Pageable pageable);
    Page<Post> findAllByCategoryNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String categoryName, String title, Pageable pageable);
    Page<Post> findAllByTagsIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(Long tagId, Pageable pageable);
    Page<Post> findAllByTagsNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String tagName, Pageable pageable);
    Page<Post> findAllByTagsNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String tagName, String title, Pageable pageable);
    Page<Post> findAllByUserIdAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(Long userId, Boolean valid, Pageable pageable);
    Page<Post> findAllByUserUsernameAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String username, Boolean valid, Pageable pageable);
    Page<Post> findAllByUserUsernameAndTitleContainingAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String username, String title, Boolean valid, Pageable pageable);
    Optional<Post> findByIdAndUserDeletedIsFalse(Long id);
    Optional<Post> findByTitleAndValidIsTrueAndUserDeletedIsFalse(String title);
    boolean existsByTitle(String title);
}
