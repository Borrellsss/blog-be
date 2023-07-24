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
@ToString(exclude = {"posts", "votes", "comments"})
@EntityListeners(value = {
    GlobalAuditListener.class
})
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;
    @Column(nullable = false)
    private LocalDate birthdate;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 50, nullable = false, unique = true)
    private String username;
    @Column(length = 100, nullable = false)
    private String password;
    private String avatar;
    @Column(nullable = false)
    private boolean notifications;
    @Column(nullable = false)
    private boolean blocked = false;
    @Column(nullable = false)
    private boolean deleted = false;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Vote> votes = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    public User(String firstName, String lastName, LocalDate birthdate, String email, String username, String password, boolean notifications, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.username = username;
        this.password = password;
        this.notifications = notifications;
        this.role = role;
    }
    public User(String firstName, String lastName, LocalDate birthdate, String email, String username, String password, String avatar, boolean notifications, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.notifications = notifications;
        this.role = role;
    }
}
