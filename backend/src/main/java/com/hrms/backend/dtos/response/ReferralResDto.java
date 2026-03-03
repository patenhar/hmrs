package com.hrms.backend.dtos.response;

import com.hrms.backend.enums.ReferralStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReferralResDto {
    private UUID pkReferralId;
    private JobResDto job;
    private UserResDto user;
    private String name;
    private String email;
    private String note;
    private DocumentResDto cv;
    private ReferralStatus referralStatus;
}
