package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Profile_statuses")
@Getter @Setter
public class ProfileStatus extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkProfileStatusId;

    @NotBlank
    private String profileStatusName;

    @OneToMany(mappedBy = "profileStatus")
    private List<Profile> profiles;
}
