package com.hrms.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter @Setter
public class DocumentReqDto {

    @NotBlank
    private UUID fkDocumentTypeId;

    private MultipartFile file;

    private UUID userTravelId;
}
