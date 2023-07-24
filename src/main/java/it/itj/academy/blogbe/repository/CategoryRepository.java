package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Page<Category> findAllByOrderByName(Pageable pageable);
    Page<Category> findAllByNameContainingOrderByName(String name, Pageable pageable);
    boolean existsByName(String name);
}
