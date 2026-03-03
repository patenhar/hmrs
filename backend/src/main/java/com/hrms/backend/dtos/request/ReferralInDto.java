package com.hrms.backend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferralInDto {
    private String name;
    private String email;
    private String jobTitle;
    private String status;
    private Integer page;
    private Integer size;
    private String sort;
}
