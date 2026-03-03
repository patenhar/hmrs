package com.hrms.backend.dtos.request;

import com.hrms.backend.enums.DocumentType;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class DocumentReqDto {
    @NotNull(message = "Document type is required", groups = {OnCreate.class, OnUpdate.class})
    private DocumentType documentType;

    @NotNull(message = "File is required", groups = {OnCreate.class, OnUpdate.class})
    private MultipartFile file;
}
