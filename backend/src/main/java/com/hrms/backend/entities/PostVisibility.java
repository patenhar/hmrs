package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Post_Visibilities")
@Getter
@Setter
public class PostVisibility extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pk_post_visisbility_id")
    private UUID pkPostVisibilityId;

    @NotBlank
    private String visibility;
}
