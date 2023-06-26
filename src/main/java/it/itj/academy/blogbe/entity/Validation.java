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
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EntityListeners(value = {
    GlobalAuditListener.class
})
@Entity
@Table(name = "validation")
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, columnDefinition = "CHAR(8)")
    private String code;
    @Column(length = 100, nullable = false, unique = true)
    private String field;
    @Column(name = "not_null", nullable = false)
    private boolean notNull = false;
    @Column(name = "not_empty", nullable = false)
    private boolean notEmpty = false;
    private Integer min;
    private Integer max;
    @Column(columnDefinition = "TEXT")
    private String regex;
    @Column(name = "upper_case_letters")
    private Byte minUpperCaseLetters;
    @Column(name = "lower_case_letters")
    private Byte minLowerCaseLetters;
    private Byte minDigits;
    @Column(name = "special_characters")
    private Byte minSpecialCharacters;
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
    @OneToMany(mappedBy = "validation", cascade = CascadeType.ALL)
    private List<ErrorMessage> errorMessages = new ArrayList<>();

    public Validation(String field, Integer min, Integer max) {
        this.field = field;
        this.min = min;
        this.max = max;
    }
    public Validation(String field, boolean notNull, boolean notEmpty, Integer min, Integer max, Byte minUpperCaseLetters, Byte minLowerCaseLetters, Byte minDigits, Byte minSpecialCharacters) {
        this.field = field;
        this.notNull = notNull;
        this.notEmpty = notEmpty;
        this.min = min;
        this.max = max;
        this.minUpperCaseLetters = minUpperCaseLetters;
        this.minLowerCaseLetters = minLowerCaseLetters;
        this.minDigits = minDigits;
        this.minSpecialCharacters = minSpecialCharacters;
    }
    public Validation(String field, boolean notNull, boolean notEmpty, Integer min, Integer max, String regex, Byte minUpperCaseLetters, Byte minLowerCaseLetters, Byte minDigits, Byte minSpecialCharacters) {
        this.field = field;
        this.notNull = notNull;
        this.notEmpty = notEmpty;
        this.min = min;
        this.max = max;
        this.regex = regex;
        this.minUpperCaseLetters = minUpperCaseLetters;
        this.minLowerCaseLetters = minLowerCaseLetters;
        this.minDigits = minDigits;
        this.minSpecialCharacters = minSpecialCharacters;
    }
}
