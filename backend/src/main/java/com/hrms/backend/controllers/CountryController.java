package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.CountryReqDto;
import com.hrms.backend.dtos.response.CountryResDto;
import com.hrms.backend.entities.Country;
import com.hrms.backend.services.CountryService;
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
@RequestMapping("api/countries")
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<CountryResDto>>> getAllCountries() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All countries fetched successfully", countryService.getAllCountries()));
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<CountryResDto>>> getCountriesByName(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All countries fetched successfully", countryService.getCountriesByName(name)));
    }


    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<CountryResDto>> getCountryById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Country fetched successfully", countryService.getCountryById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<CountryResDto>> addCountry(@RequestBody @Validated(OnCreate.class) CountryReqDto countryReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Country added successfully", countryService.addCountry(countryReqDto)));
    }

}
