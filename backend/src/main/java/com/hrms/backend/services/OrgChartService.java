package com.hrms.backend.services;

import com.hrms.backend.dtos.response.OrgChartResDto;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.repos.ProfileRepo;
import com.hrms.backend.services.interfaces.IOrgChartService;
import com.hrms.backend.utils.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrgChartService implements IOrgChartService {
    private final ProfileRepo profileRepo;
    OrgChartService(ProfileRepo profileRepo) {
        this.profileRepo = profileRepo;
    }
    @Override
    public OrgChartResDto getOrgChart(UUID profileId) {
        Profile profile = profileRepo.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        Profile manager = profile.getManagerProfile();
        List<Profile> managers = new ArrayList<>();
        while(manager != null) {
            managers.add(manager);
            manager = manager.getManagerProfile();
        }
        return new OrgChartResDto(profile, managers);
    }
}
