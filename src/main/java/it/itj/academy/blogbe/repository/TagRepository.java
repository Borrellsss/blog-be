package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    Page<Tag> findAllByOrderByName(Pageable pageable);
    Page<Tag> findAllByNameContainingOrderByName(String name, Pageable pageable);
    List<Tag> findAllByCategoriesIdOrderByName(Long categoryId);
    Page<Tag> findAllByCategoriesNameOrderByName(String categoryName, Pageable pageable);
    boolean existsByName(String name);
}
