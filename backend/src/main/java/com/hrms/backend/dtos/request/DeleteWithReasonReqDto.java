package com.hrms.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteWithReasonReqDto {
    @NotBlank(message = "Remarks are required when removing content")
    private String remarks;
}
