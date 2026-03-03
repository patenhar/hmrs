package com.hrms.backend.dtos.response;

import com.hrms.backend.enums.JobStakeHolderType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class JobStakeHolderResDtoForJob {
    private UUID jobStakeHolderId;
    private UserResDto user;
    private JobStakeHolderType jobStakeHolderType;
}
