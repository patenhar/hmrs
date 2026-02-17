package com.hrms.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Job_stakeholders")
@Getter
@Setter
public class JobStakeHolder extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobStakeHolderId;

    @ManyToOne
    @JoinColumn(name = "fk_job_id", referencedColumnName = "pkJobId")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "fk_job_stakeholder_type_id", referencedColumnName = "pkJobStakeHolderTypeId")
    private JobStakeHolderType jobStakeHolderType;
}
