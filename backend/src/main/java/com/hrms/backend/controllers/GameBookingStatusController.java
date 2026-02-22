package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.GameBookingStatusReqDto;
import com.hrms.backend.dtos.response.GameBookingStatusResDto;
import com.hrms.backend.entities.GameBookingStatus;
import com.hrms.backend.services.GameBookingStatusService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/gameBooking-statuses")
public class GameBookingStatusController {
    private final GameBookingStatusService gameBookingStatusService;

    @Autowired
    public GameBookingStatusController(GameBookingStatusService gameBookingStatusService) {
        this.gameBookingStatusService = gameBookingStatusService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<GameBookingStatusResDto>>> getAllGameBookingStatuses() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All gameBooking statuses fetched successfully", gameBookingStatusService.getAllGameBookingStatuses()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<GameBookingStatusResDto>> getGameBookingStatusById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("GameBooking Status fetched successfully", gameBookingStatusService.getGameBookingStatusById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<GameBookingStatusResDto>> addGameBookingStatus(@RequestBody @Validated(OnCreate.class) GameBookingStatusReqDto gameBookingStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("GameBooking Status added successfully", gameBookingStatusService.addGameBookingStatus(gameBookingStatusReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<GameBookingStatusResDto>> updateGameBookingStatus(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) GameBookingStatusReqDto gameBookingStatusReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("GameBooking Status updated successfully", gameBookingStatusService.updateGameBookingStatus(id, gameBookingStatusReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteGameBookingStatus(@PathVariable UUID id) {
        String res = "GameBooking Status not deleted";
        if (gameBookingStatusService.deleteGameBookingStatus(id)){
            res = "GameBooking Status deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
