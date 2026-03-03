package com.hrms.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Posts")
@Getter
@Setter
@JsonIgnoreProperties({"likes", "comments"})
public class Post extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkPostId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Column(columnDefinition = "bit default 0")
    private Boolean isDeleted = false;

    @Column(columnDefinition = "bit default 0")
    private Boolean isSystemGenerated = false;

    private String remarks;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User author;

    @ManyToOne
    @JoinColumn(name = "fk_post_visibility_id")
    private PostVisibility visibility;

    @ManyToMany
    @JoinTable(
            name = "Post_Tags",
            joinColumns = @JoinColumn(name = "fk_post_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();
}
