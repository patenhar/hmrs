package com.hrms.backend.dtos.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralSpecDto {
    private String name;
    private String email;
    private String jobTitle;
    private String status;
}
