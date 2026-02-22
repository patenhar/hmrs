package com.hrms.backend.services;

import com.hrms.backend.dtos.response.OrgChartResDto;
import com.hrms.backend.dtos.response.ProfileResDto;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.repos.ProfileRepo;
import com.hrms.backend.services.interfaces.IOrgChartService;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrgChartService implements IOrgChartService {
    private final ProfileService profileService;

    OrgChartService(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public OrgChartResDto getOrgChart(UUID profileId) {
        ProfileResDto profileResDto = profileService.findProfileById(profileId);
        List<ProfileResDto> directReports = profileService.findDirectReports(profileId);

        ProfileResDto manager = profileResDto.getManagerProfile();
        List<ProfileResDto> managers = new ArrayList<>();
        while(manager != null) {
            managers.add(manager);
            manager = manager.getManagerProfile();
        }

        return new OrgChartResDto(profileResDto, managers, directReports);
    }
}
