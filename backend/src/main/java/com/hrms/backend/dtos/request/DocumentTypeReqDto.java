package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;

public class DocumentTypeReqDto {
    @NotBlank(message = "Document type name is required", groups = {OnCreate.class, OnUpdate.class})
    private String documentTypeName;
}
