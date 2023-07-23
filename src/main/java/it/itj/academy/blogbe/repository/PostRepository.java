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
            GROUP BY p.id
            ORDER BY comments_count DESC;
        """,
        nativeQuery = true
    )
    Page<Post> findAllByOrderByCommentsDesc(Pageable pageable);
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
            ORDER BY post_likes_count.count DESC;
        """,
        nativeQuery = true
    )
    Page<Post> findAllByVotesIsTrueOrderByVotesDesc(Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Optional<Post> findByTitle(String title);
    Page<Post> findAllByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);
    Page<Post> findAllByCategoryIdOrderByCreatedAtDesc(Long categoryId, Pageable pageable);
    Page<Post> findAllByCategoryNameOrderByCreatedAtDesc(String categoryName, Pageable pageable);
    Page<Post> findAllByCategoryNameAndTitleContainingOrderByCreatedAtDesc(String categoryName, String title, Pageable pageable);
    Page<Post> findAllByTagsIdOrderByCreatedAtDesc(Long tagId, Pageable pageable);
    Page<Post> findAllByTagsNameOrderByCreatedAtDesc(String tagName, Pageable pageable);
    Page<Post> findAllByTagsNameAndTitleContainingOrderByCreatedAtDesc(String tagName, String title, Pageable pageable);
    Page<Post> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Post> findAllByUserUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
    Page<Post> findAllByUserUsernameAndTitleContainingOrderByCreatedAtDesc(String username, String title, Pageable pageable);
    Page<Post> findAllByValidOrderByCreatedAtDesc(Boolean valid, Pageable pageable);
    boolean existsByTitle(String title);
}
