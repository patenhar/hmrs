package com.hrms.backend.services;

import com.hrms.backend.dtos.response.CurrentUserResDto;
import com.hrms.backend.dtos.response.ProfileResDtoForUser;
import com.hrms.backend.dtos.response.UserResDto;
import com.hrms.backend.dtos.request.UserInDto;
import com.hrms.backend.dtos.spec.UserSpecDto;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.User;
import com.hrms.backend.repos.*;
import com.hrms.backend.services.interfaces.IUserService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.utils.JsonStringToSortDto;
import com.hrms.backend.utils.ResourceNotFoundException;
import com.hrms.backend.dtos.spec.SortDto;
import com.hrms.backend.utils.UserInfo;
import com.hrms.backend.utils.UserSpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ProfileRepo profileRepo;
    private final ModelMapper modelMapper;
    private final LikeRepo likeRepo;
    private final NotificationRepo notificationRepo;
    private final PostRepo postRepo;
    private final GameBookingRepo gameBookingRepo;
    private final TravelDocumentRepo travelDocumentRepo;
    private final JobStakeHolderRepo jobStakeHolderRepo;
    private final JobShareRecordRepo jobShareRecordRepo;
    private final ExpenseRepo expenseRepo;
    private final ReferralRepo referralRepo;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, ProfileRepo profileRepo, ModelMapper modelMapper,
                       LikeRepo likeRepo, NotificationRepo notificationRepo, PostRepo postRepo,
                       GameBookingRepo gameBookingRepo, TravelDocumentRepo travelDocumentRepo,
                       JobStakeHolderRepo jobStakeHolderRepo, JobShareRecordRepo jobShareRecordRepo,
                       ExpenseRepo expenseRepo, ReferralRepo referralRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.profileRepo = profileRepo;
        this.modelMapper = modelMapper;
        this.likeRepo = likeRepo;
        this.notificationRepo = notificationRepo;
        this.postRepo = postRepo;
        this.gameBookingRepo = gameBookingRepo;
        this.travelDocumentRepo = travelDocumentRepo;
        this.jobStakeHolderRepo = jobStakeHolderRepo;
        this.jobShareRecordRepo = jobShareRecordRepo;
        this.expenseRepo = expenseRepo;
        this.referralRepo = referralRepo;
    }

    public CurrentUserResDto getCurrentUser() {
        User user = userRepo.findById(getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CurrentUserResDto dto = modelMapper.map(user, CurrentUserResDto.class);
        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            dto.setAuthorities(user.getRole().getPermissions().stream()
                    .map(p -> p.getPermissionName().toUpperCase())
                    .toList());
        }
        Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
        if (profile != null) {
            dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
        }
        return dto;
    }

    public UserResDto findUserById(UUID id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserResDto dto = modelMapper.map(user, UserResDto.class);
        Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
        if (profile != null) {
            dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
        }
        return dto;
    }

    public User getAuthenticatedUser() {
        return findById(getCurrentUserId());
    }

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new BadCredentialsException("Unauthorized");
        }
        if (!(auth.getPrincipal() instanceof UserInfo userInfo)) {
            throw new BadCredentialsException("Unauthorized");
        }
        return userInfo.getUserId();
    }

    public User findById(UUID id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public ApiResponse<List<UserResDto>> getAll() {
        List<UserResDto> users = userRepo.findAll().stream()
                .map(user -> {
                    UserResDto dto = modelMapper.map(user, UserResDto.class);
                    Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
                    if (profile != null) {
                        dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
                    }
                    return dto;
                })
                .toList();
        return new ApiResponse<>("Users fetched successfully", users);
    }

    public List<User> getUsersByRoleName(String roleName) {
        return userRepo.findAllByRole_RoleNameIgnoreCase(roleName);
    }

    public List<UserResDto> getAllUsersByEmail(String email) {
        return userRepo.findAllByEmailContainingIgnoreCase(email).stream().map(user -> {
            UserResDto dto = modelMapper.map(user, UserResDto.class);
            Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
            if (profile != null) {
                dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
            }
            return dto;
        }).toList();
    }

    public List<UserResDto> getUsersByGameAndProfileName(UUID gameId, String name) {
        String normalizedName = name == null ? "" : name.trim();
        return userRepo.findUsersByGameInterestAndProfileName(gameId, normalizedName)
                .stream()
                .map(user -> {
                    UserResDto dto = modelMapper.map(user, UserResDto.class);
                    Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
                    if (profile != null) {
                        dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
                    }
                    return dto;
                })
                .toList();
    }

    @Override
    public ApiResponse<UserResDto> getById(UUID id) {
        User user = findById(id);
        UserResDto dto = modelMapper.map(user, UserResDto.class);
        Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
        if (profile != null) {
            dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
        }
        return new ApiResponse<>("User fetched successfully", dto);
    }

    public UserResDto getUserById(UUID id) {
        User user = findById(id);
        UserResDto dto = modelMapper.map(user, UserResDto.class);
        Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
        if (profile != null) {
            dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
        }
        return dto;
    }

    @Override
    public ApiResponse<UserResDto> updateRole(UUID id, UUID roleId) {
        User user = findById(id);
        user.setRole(roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("invalid role id")));
        User updatedUser = userRepo.save(user);
        UserResDto dto = modelMapper.map(updatedUser, UserResDto.class);
        Profile profile = profileRepo.findProfileByUser_PkUserId(updatedUser.getPkUserId());
        if (profile != null) {
            dto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
        }
        return new ApiResponse<>("User role updated successfully", dto);
    }

    public Page<UserResDto> searchUsersWithPaginationSortingAndFiltering(UserInDto dto) {
        UserSpecDto specDto = UserSpecDto.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .roleName(dto.getRoleName())
                .build();

        List<SortDto> sortDtos = JsonStringToSortDto.jsonStringToSortDto(dto.getSort());
        List<Sort.Order> orders = new ArrayList<>();
        if (sortDtos != null) {
            for (SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, mapSortField(sortDto.getField())));
            }
        }

        PageRequest pageRequest = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                Sort.by(orders)
        );

        Specification<User> specification = UserSpecification.getSpecification(specDto);
        Page<User> users = userRepo.findAll(specification, pageRequest);
        return users.map(user -> {
            UserResDto userResDto = modelMapper.map(user, UserResDto.class);
            Profile profile = profileRepo.findProfileByUser_PkUserId(user.getPkUserId());
            if (profile != null) {
                userResDto.setProfile(modelMapper.map(profile, ProfileResDtoForUser.class));
            }
            return userResDto;
        });
    }

    private String mapSortField(String field) {
        return switch (field) {
            case "currentRole" -> "role.roleName";
            default -> field;
        };
    }

    @Override
    @Transactional
    public ApiResponse<String> delete(UUID id) {
        findById(id);
        travelDocumentRepo.clearUploadedByUserId(id);
        postRepo.clearAuthorByUserId(id);
        gameBookingRepo.clearBookedByUserId(id);
        expenseRepo.clearLastActionByUserId(id);
        referralRepo.clearUserByUserId(id);
        gameBookingRepo.removeUserFromAllTeams(id);
        likeRepo.deleteAllByUser_PkUserId(id);
        notificationRepo.deleteAllByUser_PkUserId(id);
        jobStakeHolderRepo.deleteAllByUser_PkUserId(id);
        jobShareRecordRepo.deleteAllByUser_PkUserId(id);
        userRepo.deleteById(id);
        return new ApiResponse<>("User deleted successfully", null);
    }
}
