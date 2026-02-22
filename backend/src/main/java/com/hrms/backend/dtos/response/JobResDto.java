package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Document;
import com.hrms.backend.entities.JobShareRecord;
import com.hrms.backend.entities.JobStakeHolder;
import com.hrms.backend.entities.Referral;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class JobResDto {
    private UUID pkJobId;
    private String title;
    private String description;
    private DocumentResDto jd;
    private List<JobStakeHolderResDtoForJob> jobStakeHolders;
}
