package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Long>, PagingAndSortingRepository<Validation, Long> {
    Optional<Validation> findByCode(String code);
    Optional<Validation> findByField(String field);
    List<Validation> findByFieldStartsWith(String field);
}
