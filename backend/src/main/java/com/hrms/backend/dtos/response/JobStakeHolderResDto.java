package com.hrms.backend.dtos.response;

import com.hrms.backend.enums.JobStakeHolderType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class JobStakeHolderResDto {
    private UUID jobStakeHolderId;
    private JobResDto jobResDto;
    private UserResDto userResDto;
    private JobStakeHolderType jobStakeHolderTypeResDto;
}
