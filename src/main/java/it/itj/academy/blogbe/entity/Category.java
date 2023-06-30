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
@ToString(exclude = {"posts"})
@EntityListeners(value = {
    GlobalAuditListener.class
})
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 150, nullable = false, unique = true)
    private String name;
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
    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();
    @ManyToMany(mappedBy = "categories")
    private List<Tag> tags = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}
