package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

@Getter @Setter
public class JobShareReqDto {

    @NotBlank(message = "Email is required", groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Job is required", groups = {OnCreate.class, OnUpdate.class})
    @UUID(message = "Invalid UUID format")
    private java.util.UUID jobId;
}
