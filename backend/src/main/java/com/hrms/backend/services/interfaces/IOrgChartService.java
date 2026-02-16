package com.hrms.backend.services.interfaces;

import com.hrms.backend.dtos.response.OrgChartResDto;

import java.util.UUID;

public interface IOrgChartService {
    OrgChartResDto getOrgChart(UUID profileId);
}
