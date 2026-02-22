package com.hrms.backend.controllers;

import com.hrms.backend.dtos.response.OrgChartResDto;
import com.hrms.backend.services.OrgChartService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/profile")
public class OrgChartController {
    private final OrgChartService orgChartService;

    @Autowired
    public OrgChartController(OrgChartService orgChartService) {
        this.orgChartService = orgChartService;
    }

    @GetMapping("/{id}/org-chart")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<OrgChartResDto>> getOrgChartById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("OrgChart fetched successfully", orgChartService.getOrgChart(id)));
    }
}
