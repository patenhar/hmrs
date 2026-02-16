package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "referrals")
@Getter
@Setter
public class Referral extends AuditableTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkReferralId;

    @ManyToOne
    @JoinColumn(name = "fk_job_id", referencedColumnName = "pkJobId")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "pkUserId")
    private User user;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String note;

    @OneToOne
    @JoinColumn(name = "fk_docuemnt_id", referencedColumnName = "pkDocumentId")
    private Document cv;

    @ManyToOne
    @JoinColumn(name = "fk_referral_status_id")
    private ReferralStatus referralStatus;
}
