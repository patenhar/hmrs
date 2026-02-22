package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class JobStakeHolderResDtoForJob {
    private UUID jobStakeHolderId;
    private UserResDto user;
    private JobStakeHolderTypeResDto jobStakeHolderType;
}
