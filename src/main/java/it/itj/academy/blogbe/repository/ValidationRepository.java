package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Long> {
    Optional<Validation> findByCode(String code);
    Optional<Validation> findByField(String field);
    List<Validation> findByFieldStartsWith(String field);
}
