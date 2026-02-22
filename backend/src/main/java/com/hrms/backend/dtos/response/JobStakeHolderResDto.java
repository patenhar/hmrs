package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class JobStakeHolderResDto {
    private UUID jobStakeHolderId;
    private JobResDto jobResDto;
    private UserResDto userResDto;
    private JobStakeHolderTypeResDto jobStakeHolderTypeResDto;
}
