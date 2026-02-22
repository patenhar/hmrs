package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ProfileReqDto;
import com.hrms.backend.dtos.response.ProfileResDto;
import com.hrms.backend.entities.Department;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.ProfileStatus;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.ProfileRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {
    private final ProfileRepo profileRepo;
    private final ModelMapper modelMapper;
    private final DepartmentService departmentService;
    private final ProfileStatusService profileStatusService;
    private final UserService userService;

    public ProfileService(ProfileRepo profileRepo, ModelMapper modelMapper, DepartmentService departmentService, ProfileStatusService profileStatusService, UserService userService) {
        this.profileRepo = profileRepo;
        this.modelMapper = modelMapper;
        this.departmentService = departmentService;
        this.profileStatusService = profileStatusService;
        this.userService = userService;
    }

    public ProfileResDto findProfileById(UUID id) {
        Profile profile = profileRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return modelMapper.map(profile, ProfileResDto.class);
    }

    public ProfileResDto findProfileByUser_PkUserId(UUID id) {
        return modelMapper.map(profileRepo.findProfileByUser_PkUserId(id), ProfileResDto.class);
    }

    public List<ProfileResDto> findDirectReports(UUID id) {
        return profileRepo.findDirectReports(id).stream().map(profile -> modelMapper.map(profile, ProfileResDto.class)).toList();
    }

    public List<ProfileResDto> getAllProfiles() {
        return profileRepo.findAll().stream().map(st -> modelMapper.map(st, ProfileResDto.class)).toList();
    }

    public ProfileResDto getProfileById(UUID id) {
        return findProfileById(id);
    }

    public ProfileResDto addProfile(ProfileReqDto profileReqDto) {
        Profile profile = modelMapper.map(profileReqDto, Profile.class);
        profile.setUser(userService.getAuthenticatedUser());
        profile.setDepartment(modelMapper.map(departmentService.findDepartmentById(profileReqDto.getDepartmentId()), Department.class));
        profile.setProfileStatus(modelMapper.map(profileStatusService.findProfileStatusById(UUID.fromString("729f81b4-782a-48b4-b92d-2b40e35ff7da")), ProfileStatus.class));
        if (profileReqDto.getManagerProfileId() != null) {
            profile.setManagerProfile(modelMapper.map(findProfileByUser_PkUserId(profileReqDto.getManagerProfileId()), Profile.class));
        }
        return modelMapper.map(profileRepo.save(profile), ProfileResDto.class);
    }

    public ProfileResDto updateProfile(UUID id, ProfileReqDto profileReqDto) {
        ProfileResDto profile = findProfileById(id);
        modelMapper.map(profileReqDto, profile);
        Profile updatedProfile =  profileRepo.save(modelMapper.map(profileReqDto, Profile.class));
        return modelMapper.map(updatedProfile, ProfileResDto.class);
    }

    public boolean deleteProfile(UUID id) {
        findProfileById(id);
        profileRepo.deleteById(id);
        return true;
    }
}
