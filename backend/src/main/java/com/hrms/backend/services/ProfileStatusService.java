package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ProfileStatusReqDto;
import com.hrms.backend.dtos.response.ProfileStatusResDto;
import com.hrms.backend.entities.ProfileStatus;
import com.hrms.backend.entities.ProfileStatus;
import com.hrms.backend.repos.ProfileStatusRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProfileStatusService {

    private final ProfileStatusRepo profileStatusRepo;
    private final ModelMapper modelMapper;

    public ProfileStatusService(ProfileStatusRepo profileStatusRepo, ModelMapper modelMapper) {
        this.profileStatusRepo = profileStatusRepo;
        this.modelMapper = modelMapper;
    }

    public ProfileStatusResDto findProfileStatusById(UUID id) {
        ProfileStatus profileStatus = profileStatusRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile status not found"));
        return modelMapper.map(profileStatus, ProfileStatusResDto.class);
    }

    public List<ProfileStatusResDto> getAllProfileStatuses() {
        return profileStatusRepo.findAll().stream().map(st -> modelMapper.map(st, ProfileStatusResDto.class)).toList();
    }

    public ProfileStatusResDto getProfileStatusById(UUID id) {
        return findProfileStatusById(id);
    }

    public ProfileStatusResDto addProfileStatus(ProfileStatusReqDto profileStatusReqDto) {
        ProfileStatus profileStatus = profileStatusRepo.save(modelMapper.map(profileStatusReqDto, ProfileStatus.class));
        return modelMapper.map(profileStatus, ProfileStatusResDto.class);
    }

    public ProfileStatusResDto updateProfileStatus(UUID id, ProfileStatusReqDto profileStatusReqDto) {
        ProfileStatusResDto profileStatus = findProfileStatusById(id);
        modelMapper.map(profileStatusReqDto, profileStatus);
        ProfileStatus updatedProfileStatus = profileStatusRepo.save(modelMapper.map(profileStatusReqDto, ProfileStatus.class));
        return modelMapper.map(updatedProfileStatus, ProfileStatusResDto.class);
    }

    public boolean deleteProfileStatus(UUID id) {
        findProfileStatusById(id);
        profileStatusRepo.deleteById(id);
        return true;
    }
}
