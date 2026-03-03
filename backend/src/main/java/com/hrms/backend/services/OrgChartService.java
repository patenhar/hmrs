package com.hrms.backend.services;

import com.hrms.backend.dtos.response.OrgChartResDto;
import com.hrms.backend.dtos.response.ProfileResDto;
import com.hrms.backend.dtos.response.ProfileResDtoForManager;
import com.hrms.backend.services.interfaces.IOrgChartService;
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

        List<ProfileResDto> managers = new ArrayList<>();
        ProfileResDtoForManager managerRef = profileResDto.getManagerProfile();
        while (managerRef != null) {
            ProfileResDto managerFull = profileService.findProfileById(managerRef.getPkProfileId());
            managers.add(managerFull);
            managerRef = managerFull.getManagerProfile();
        }

        return new OrgChartResDto(profileResDto, managers, directReports);
    }
}
