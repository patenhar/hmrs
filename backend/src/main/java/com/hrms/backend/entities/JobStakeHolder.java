package com.hrms.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "job_stakeholders")
@Getter
@Setter
public class JobStakeHolder extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobStakeHolderId;

    @ManyToOne()
    @JoinColumn(name = "fk_job_id")
    private Job job;

    @ManyToOne()
    @JoinColumn(name = "fk_user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "fk_job_stakeholder_type_id")
    private JobStakeHolderType jobStakeHolderType;
}
