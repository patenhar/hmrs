package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Comments")
@Getter
@Setter
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkCommentId;

    @NotBlank
    private String content;

    @Column(columnDefinition = "bit default 0")
    private Boolean isDeleted = false;

    private String remarks;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @ManyToOne
    @JoinColumn(name = "fk_post_id", referencedColumnName = "pkPostId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "fk_parent_id", referencedColumnName = "pkCommentId")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();
}
