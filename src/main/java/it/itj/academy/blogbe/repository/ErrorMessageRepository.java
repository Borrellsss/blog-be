package it.itj.academy.blogbe.repository;

import it.itj.academy.blogbe.entity.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {
    List<ErrorMessage> findByValidationCode(String validationCode);
    Optional<ErrorMessage> findByErrorType(String errorType);
    Optional<ErrorMessage> findByErrorTypeAndValidationCode(String errorType, String validationCode);
}
