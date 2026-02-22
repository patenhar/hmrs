package com.hrms.backend.dtos.request;

import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter @Setter
public class TravelDocumentReqDto {
    @NotNull(message = "User-travel is required", groups = {OnCreate.class, OnUpdate.class})
    private UUID userTravelId;

    @Valid
    private DocumentReqDto documentReqDto;
}
