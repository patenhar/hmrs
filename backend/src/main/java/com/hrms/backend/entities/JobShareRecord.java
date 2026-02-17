package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Job_share_records")
@Getter
@Setter
public class JobShareRecord extends AuditableTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobShareRecordId;

    @ManyToOne
    @JoinColumn(name = "fk_job_id", referencedColumnName = "pkJobId")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User user;

    @NotBlank
    @Email
    private String email;
}
