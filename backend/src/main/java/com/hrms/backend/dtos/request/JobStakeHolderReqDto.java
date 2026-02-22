package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class JobStakeHolderReqDto {
    @NotNull(message = "User is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID userId;

    @NotNull(message = "Stakeholder type is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID jobStakeHolderTypeId;
}
