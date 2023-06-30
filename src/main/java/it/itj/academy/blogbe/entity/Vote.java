package it.itj.academy.blogbe.entity;

import it.itj.academy.blogbe.audit.GlobalAuditListener;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"post"})
@EntityListeners(value = {
    GlobalAuditListener.class
})
@Entity
@Table(name = "vote")
@IdClass(Vote.VotePk.class)
public class Vote {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(nullable = false)
    private boolean liked;
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDate createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Vote(User user, Post post, boolean liked) {
        this.user = user;
        this.post = post;
        this.liked = liked;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class VotePk implements Serializable {
        private Long user;
        private Long post;
    }
}