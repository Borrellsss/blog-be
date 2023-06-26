package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long>, PagingAndSortingRepository<ErrorMessage, Long> {
    List<ErrorMessage> findByValidationCode(String code);
    Optional<ErrorMessage> findByValidationCodeAndErrorType(String code, String errorType);
}
