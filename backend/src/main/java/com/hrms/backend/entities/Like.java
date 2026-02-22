package com.hrms.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Likes")
@Getter
@Setter
@IdClass(LikeId.class)
public class Like {
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_post_id", referencedColumnName = "pkPostId")
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User user;

    @Column(name = "fk_commnet_id")
    private UUID fkCommentId;

    @CreationTimestamp
    private LocalDateTime likedAt;
}
