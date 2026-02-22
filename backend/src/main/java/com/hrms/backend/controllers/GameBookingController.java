package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.GameBookingReqDto;
//import com.hrms.backend.dtos.response.GameBookingResDto;
import com.hrms.backend.dtos.response.GameBookingResDto;
import com.hrms.backend.dtos.response.GameSlotResDto;
import com.hrms.backend.entities.GameBooking;
import com.hrms.backend.services.GameBookingService;
import com.hrms.backend.services.GameSlotService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/game/bookings")
public class GameBookingController {
    private final GameBookingService gameBookingService;
    private final GameSlotService gameSlotService;

    @Autowired
    public GameBookingController(GameBookingService gameBookingService, GameSlotService gameSlotService) {
        this.gameBookingService = gameBookingService;
        this.gameSlotService = gameSlotService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<GameBookingResDto>>> getAllGameBookings() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All gameBookings fetched successfully", gameBookingService.getAllGameBookings()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<GameBookingResDto>> getGameBookingById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("GameBooking fetched successfully", gameBookingService.getGameBookingById(id)));
    }

    @GetMapping("/users/{userId}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<GameBookingResDto>>> getGameBookingByUser(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("GameBooking fetched successfully", gameBookingService.getGameBookingByUser(userId)));
    }

    @GetMapping("/slots/available")
    public ResponseEntity<ApiResponse<List<GameSlotResDto>>> getAvailableSlots(@RequestParam UUID gameId, @RequestParam LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Available slots", gameSlotService.getGameSlotsByGameAndDate(gameId, date)));
    }

    @PostMapping(path = "/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<GameBookingResDto>> addGameBooking( @RequestBody @Validated(OnCreate.class) GameBookingReqDto gameBookingReqDto) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("GameBooking added successfully", gameBookingService.addGameBooking(gameBookingReqDto)));
    }


//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
//    public ResponseEntity<ApiResponse<String>> deleteGameBooking(@PathVariable UUID id) {
//        String res = "GameBooking could not be deleted";
//        if (gameBookingService.deleteGameBooking(id)){
//            res = "GameBooking deleted successfully";
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
//    }
}
