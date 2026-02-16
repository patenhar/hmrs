package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "Referral_statuses")
@Getter
@Setter
public class ReferralStatus extends AuditableTimestamp{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkReferralStatusId;

    @NotBlank
    private String referralStatusName;
}
