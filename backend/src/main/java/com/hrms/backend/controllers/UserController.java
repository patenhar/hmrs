package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.UserInDto;
import com.hrms.backend.dtos.response.CurrentUserResDto;
import com.hrms.backend.dtos.response.NotificationResDto;
import com.hrms.backend.dtos.response.UserResDto;
import com.hrms.backend.services.NotificationService;
import com.hrms.backend.services.UserService;
import com.hrms.backend.utils.ApiResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, NotificationService notificationService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
    }

    @GetMapping("/me")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<CurrentUserResDto>> getCurrentUser() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Current user fetched", userService.getCurrentUser()));
    }

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<List<NotificationResDto>>> getNotifications() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Notifications fetched successfully", notificationService.getNotificationsForUser()));
    }

    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<ApiResponse<String>> markNotificationAsRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Notification marked as read", null));
    }


    @GetMapping("/filtering&pagination&sorting")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<Page<UserResDto>> getUsersPaginated(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", defaultValue = "[{\"field\":\"email\",\"direction\":\"asc\"}]") String sort,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "role-name", required = false) String roleName
    ) {
        Page<UserResDto> users = userService.searchUsersWithPaginationSortingAndFiltering(
                UserInDto.builder()
                        .email(normalizeOptionalString(email))
                        .name(normalizeOptionalString(name))
                        .roleName(normalizeOptionalString(roleName))
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .build());
        return ResponseEntity.ok(users);
    }

    private String normalizeOptionalString(String value) {
        if (value == null) return null;
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<List<UserResDto>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<UserResDto>>> getAllUsersByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Users fetched by email", userService.getAllUsersByEmail(email)));
    }

    @GetMapping("/search/by-game")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<UserResDto>>> getUsersByGameAndName(
            @RequestParam UUID gameId,
            @RequestParam(defaultValue = "") String name
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Users fetched by game and profile name", userService.getUsersByGameAndProfileName(gameId, name)));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<UserResDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getById(id));
    }

//    @GetMapping("/{id}")
//   @PreAuthorize("hasAuthority('VIEW_USER')")
//    public ResponseEntity<ApiResponse<UserResDto>> getById(@PathVariable UUID id) {
//        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User fetched", userService.getUserById(id)));
//    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<UserResDto>> updateRole(@PathVariable UUID id, @RequestBody UUID roleId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateRole(id, roleId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_USER')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.delete(id));
    }
}
