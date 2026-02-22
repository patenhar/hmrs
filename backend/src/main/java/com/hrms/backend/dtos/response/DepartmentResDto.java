package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DepartmentResDto {
    private UUID pkDepartmentId;
    private String departmentName;
}
