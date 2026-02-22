package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.CityReqDto;
import com.hrms.backend.dtos.response.CityResDto;
import com.hrms.backend.entities.City;
import com.hrms.backend.services.CityService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cities")
public class CityController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<CityResDto>>> getAllCities() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All cities fetched successfully", cityService.getAllCities()));
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<CityResDto>>> findCitiesByCountryId(@RequestParam String name, @RequestParam UUID countryId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All cities fetched successfully", cityService.findCitiesByCountryId(name, countryId)));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<CityResDto>> getCityById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("City fetched successfully", cityService.getCityById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<CityResDto>> addCity(@RequestBody @Validated(OnCreate.class) CityReqDto cityReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("City added successfully", cityService.addCity(cityReqDto)));
    }

}
