package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.TravelReqDto;
import com.hrms.backend.dtos.response.ExpenseResDto;
import com.hrms.backend.dtos.response.TravelResDto;
import com.hrms.backend.dtos.response.UserTravelResDtoForTravel;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.services.ExpenseService;
import com.hrms.backend.services.TravelService;
import com.hrms.backend.services.UserTravelService;
import com.hrms.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/travels")
public class TravelController {
    private final TravelService travelService;
    private final UserTravelService userTravelService;
    private final ExpenseService expenseService;

    @Autowired
    public TravelController(TravelService travelService, UserTravelService userTravelService, ExpenseService expenseService) {
        this.travelService = travelService;
        this.userTravelService = userTravelService;
        this.expenseService = expenseService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<List<TravelResDto>>> getAllTravels() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All travels fetched successfully", travelService.getAllTravels()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<TravelResDto>> getTravelById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel fetched successfully", travelService.getTravelById(id)));
    }

    @GetMapping("/users/{id}")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<List<TravelResDto>>> getTravelByUserId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel fetched for user successfully", travelService.getTravelByUserId(id)));
    }

    @GetMapping("/users/{userTravelId}/expenses")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<List<ExpenseResDto>>> getExpenseByUserId(@PathVariable UUID userTravelId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All user expenses fetched successfully", expenseService.getExpenseByUserTravelId(userTravelId)));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<ApiResponse<List<UserTravelResDtoForTravel>>> getTravelUsers(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel fetched for user successfully", userTravelService.getTravelUsers(id)));
    }
    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_TRAVEL')")
    public ResponseEntity<ApiResponse<TravelResDto>> addTravel(@RequestBody TravelReqDto travelDto) {
        String res = "Travel could not be added";
        if(travelService.addTravel(travelDto)) {
            res = "Travel added successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_TRAVEL')")
    public ResponseEntity<ApiResponse<TravelResDto>> updateTravel(@PathVariable UUID id, @RequestBody TravelReqDto travelDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel updated successfully", travelService.updateTravel(id, travelDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_TRAVEL')")
    public ResponseEntity<ApiResponse<String>> deleteTravel(@PathVariable UUID id) {
        String res = "Travel could not be deleted";
        if (travelService.deleteTravel(id)) {
            res = "Travel deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null));
    }
}
