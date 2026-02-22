package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Profiles")
@Getter
@Setter
public class Profile extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkProfileId;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private LocalDate joiningDate;

    @OneToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_manager_id")
    private Profile managerProfile;

    @OneToMany(mappedBy = "managerProfile")
    private List<Profile> teamMembers;

    @ManyToOne
    @JoinColumn(name = "fk_deparment_id", referencedColumnName = "pkDepartmentId")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "fk_profile_status_id", referencedColumnName = "pkProfileStatusId")
    private ProfileStatus profileStatus;

    @ManyToMany
    @JoinTable(
            name = "profile_games",
            joinColumns = @JoinColumn(name = "fk_profile_id", referencedColumnName = "pkProfileId"),
            inverseJoinColumns = @JoinColumn(name = "fk_game_id", referencedColumnName = "pkGameId")
    )
    private List<Game> games = new ArrayList<>();
}
