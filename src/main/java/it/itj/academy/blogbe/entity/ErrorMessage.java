package it.itj.academy.blogbe.entity;

import it.itj.academy.blogbe.audit.GlobalAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "validation")
@EntityListeners(value = {
    GlobalAuditListener.class
})
@Entity
@Table(name = "error_message")
public class ErrorMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "error_type", nullable = false, unique = true)
    private String errorType;
    @Column(nullable = false, unique = true)
    private String message;
    @Column(name = "created_by")
    @CreatedBy
    private Long createdBy;
    @Column(name = "updated_by")
    @LastModifiedBy
    private Long updatedBy;
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDate createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    @LastModifiedDate
    private LocalDateTime updatedAt;
    // RELATIONSHIPS
    @ManyToOne
    @JoinColumn(name = "validation_code", nullable = false)
    private Validation validation;

    public ErrorMessage(String errorType, String message, Validation validation) {
        this.errorType = errorType;
        this.message = message;
        this.validation = validation;
    }
}
