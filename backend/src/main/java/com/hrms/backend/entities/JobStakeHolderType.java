package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Job_stakeholder_types")
@Getter @Setter
public class JobStakeHolderType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkJobStakeHolderTypeId;

    @NotBlank
    private String jobStakeHolderTypeName;

    @OneToMany(mappedBy = "jobStakeHolderType")
    private List<JobStakeHolder> jobStakeHolders;
}
