package com.hrms.backend.dtos.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReferralReqDto {

    private UUID jobId;

    private String name;

    private String email;

    private String note;

    private DocumentReqDto documentReqDto;
}
