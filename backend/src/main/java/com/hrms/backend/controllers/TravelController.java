package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.TravelReqDto;
import com.hrms.backend.entities.Travel;
import com.hrms.backend.services.TravelService;
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

    @Autowired
    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<List<Travel>>> getAllTravels() {
        return ResponseEntity.status(HttpStatus.OK).body(travelService.getAllTravels());
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<Travel>> getTravelById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(travelService.getTravelById(id));
    }

    @GetMapping("/user/{id}")
//    @PreAuthorize("hasAuthority('VIEW_TRAVEL')")
    public ResponseEntity<ApiResponse<List<Travel>>> getTravelByUserId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(travelService.getTravelByUserId(id));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_TRAVEL')")
    public ResponseEntity<ApiResponse<Travel>> addTravel(@RequestBody TravelReqDto travelDto) {
        return ResponseEntity.status(HttpStatus.OK).body(travelService.addTravel(travelDto));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_TRAVEL')")
    public ResponseEntity<ApiResponse<Travel>> updateTravel(@PathVariable UUID id, @RequestBody TravelReqDto travelDto) {
        return ResponseEntity.status(HttpStatus.OK).body(travelService.updateTravel(id, travelDto));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_TRAVEL')")
    public ResponseEntity<ApiResponse<String>> deleteTravel(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(travelService.deleteTravel(id));
    }
}
