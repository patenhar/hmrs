package com.hrms.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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

    private String name;

    private LocalDate birthDate;

    private LocalDate joiningDate;

    @OneToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_manager_id")
    private Profile managerProfile;

    @OneToMany(mappedBy = "managerProfile")
    private List<Profile> teamMembers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_deparment_id", referencedColumnName = "pkDepartmentId")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_profile_status_id", referencedColumnName = "pkProfileStatusId")
    private ProfileStatus profileStatus;
}
