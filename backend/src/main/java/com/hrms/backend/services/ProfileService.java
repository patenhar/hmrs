package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ProfileReqDto;
import com.hrms.backend.dtos.response.ProfileResDto;
import com.hrms.backend.entities.Department;
import com.hrms.backend.entities.Game;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.enums.ProfileStatus;
import com.hrms.backend.repos.GameRepo;
import com.hrms.backend.repos.ProfileRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.utils.UserInfo;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {
    private final ProfileRepo profileRepo;
    private final ModelMapper modelMapper;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final GameRepo gameRepo;

    public ProfileService(ProfileRepo profileRepo, ModelMapper modelMapper, DepartmentService departmentService, UserService userService, GameRepo gameRepo) {
        this.profileRepo = profileRepo;
        this.modelMapper = modelMapper;
        this.departmentService = departmentService;
        this.userService = userService;
        this.gameRepo = gameRepo;
    }

    @Transactional(readOnly = true)
    public ProfileResDto findProfileById(UUID id) {
        Profile profile = profileRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return modelMapper.map(profile, ProfileResDto.class);
    }

    @Transactional(readOnly = true)
    public ProfileResDto findProfileByUser_PkUserId(UUID id) {
        Profile profile = profileRepo.findProfileByUser_PkUserId(id);
        if (profile == null) {
            throw new ResourceNotFoundException("Profile not found");
        }
        return modelMapper.map(profile, ProfileResDto.class);
    }

    @Transactional(readOnly = true)
    public List<ProfileResDto> findDirectReports(UUID id) {
        return profileRepo.findDirectReports(id).stream().map(profile -> modelMapper.map(profile, ProfileResDto.class)).toList();
    }

    @Transactional(readOnly = true)
    public List<ProfileResDto> getAllProfiles() {
        return profileRepo.findAll().stream().map(st -> modelMapper.map(st, ProfileResDto.class)).toList();
    }

    public ProfileResDto getProfileById(UUID id) {
        return findProfileById(id);
    }

    public ProfileResDto getProfileByUserId(UUID userId) {
        return findProfileByUser_PkUserId(userId);
    }

    public ProfileResDto addProfile(ProfileReqDto profileReqDto) {
        Profile profile = modelMapper.map(profileReqDto, Profile.class);
        profile.setUser(userService.findById(profileReqDto.getUserId()));
        profile.setDepartment(modelMapper.map(departmentService.findDepartmentById(profileReqDto.getDepartmentId()), Department.class));
        profile.setProfileStatus(ProfileStatus.ACTIVE);
        if (profileReqDto.getManagerProfileId() != null) {
            Profile managerProfile = profileRepo.findProfileByUser_PkUserId(profileReqDto.getManagerProfileId());
            if (managerProfile == null) throw new ResourceNotFoundException("Manager profile not found");
            profile.setManagerProfile(managerProfile);
        }
        List<UUID> gameIds = profileReqDto.getGameIds();
        if (gameIds != null) {
            List<Game> games = gameRepo.findAllById(gameIds);
            profile.setGames(games);
        }
        return modelMapper.map(profileRepo.save(profile), ProfileResDto.class);
    }

    public ProfileResDto updateProfile(UUID id, ProfileReqDto profileReqDto) {
        Profile existingProfile = profileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean canManageAll = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("MANAGE_ALL_PROFILE"));
        if (!canManageAll) {
            UserInfo userInfo = (UserInfo) auth.getPrincipal();
            Profile ownProfile = profileRepo.findProfileByUser_PkUserId(userInfo.getUserId());
            if (ownProfile == null || !ownProfile.getPkProfileId().equals(id)) {
                throw new AccessDeniedException("You can only update your own profile");
            }
        }

        existingProfile.setName(profileReqDto.getName());
        existingProfile.setBirthDate(profileReqDto.getBirthDate());
        existingProfile.setJoiningDate(profileReqDto.getJoiningDate());
        existingProfile.setDepartment(modelMapper.map(departmentService.findDepartmentById(profileReqDto.getDepartmentId()), Department.class));

        if (profileReqDto.getManagerProfileId() != null) {
            Profile managerProfile = profileRepo.findProfileByUser_PkUserId(profileReqDto.getManagerProfileId());
            if (managerProfile == null) throw new ResourceNotFoundException("Manager profile not found");
            existingProfile.setManagerProfile(managerProfile);
        } else {
            existingProfile.setManagerProfile(null);
        }

        if (profileReqDto.getProfileStatus() != null) {
            existingProfile.setProfileStatus(profileReqDto.getProfileStatus());
        }

        List<UUID> gameIds = profileReqDto.getGameIds();
        if (gameIds != null) {
            existingProfile.setGames(gameRepo.findAllById(gameIds));
        } else {
            existingProfile.setGames(List.of());
        }

        Profile updatedProfile = profileRepo.save(existingProfile);
        return modelMapper.map(updatedProfile, ProfileResDto.class);
    }

    public boolean deleteProfile(UUID id) {
        findProfileById(id);
        profileRepo.deleteById(id);
        return true;
    }
}
