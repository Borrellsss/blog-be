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
@ToString(exclude = {"posts", "categories"})
@EntityListeners(value = {
    GlobalAuditListener.class
})
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String description;
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
    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();
    @ManyToMany
    @JoinTable(
        name = "category_tag",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Tag(String name, String description, List<Category> categories) {
        this.name = name;
        this.description = description;
        this.categories = categories;
    }
}
