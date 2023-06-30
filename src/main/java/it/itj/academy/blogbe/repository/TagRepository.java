package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    Page<Tag> findByNameContains(String name, Pageable pageable);
    List<Tag> findByCategoriesId(Long categoryId);
    Page<Tag> findByCategoriesName(String categoryName, Pageable pageable);
    boolean existsByName(String name);
}
