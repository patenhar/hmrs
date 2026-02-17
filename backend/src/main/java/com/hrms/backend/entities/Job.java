package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Jobs")
@Getter @Setter
public class Job extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkJobId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @OneToMany(mappedBy = "job")
    private List<JobStakeHolder> jobStakeHolders;

    @OneToOne
    @JoinColumn(name = "fk_jd_id", referencedColumnName = "pkDocumentId")
    private Document jd;

    @OneToMany(mappedBy = "job")
    private List<JobShareRecord> jobShareRecords;

    @OneToMany(mappedBy = "job")
    private List<Referral> referrals;
}
