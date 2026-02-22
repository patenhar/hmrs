package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter @Setter
public class DocumentReqDto {
    @NotNull(message = "Document type is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID fkDocumentTypeId;

    @NotNull(message = "File is required", groups = {OnCreate.class, OnUpdate.class})
    private MultipartFile file;
}
