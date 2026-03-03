package com.hrms.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Tags")
@Getter
@Setter
public class Tag extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkTagId;

    @NotBlank
    private String tag;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();
}
