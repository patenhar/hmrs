package com.hrms.backend.entities;

import com.hrms.backend.enums.JobStakeHolderType;
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

    @Enumerated(EnumType.STRING)
    private JobStakeHolderType jobStakeHolderType;
}
