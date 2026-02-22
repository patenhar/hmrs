package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.AddressReqDto;
import com.hrms.backend.dtos.response.AddressResDto;
import com.hrms.backend.entities.Address;
import com.hrms.backend.services.AddressService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/addresses")
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<AddressResDto>>> getAllAddresss() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All addresses fetched successfully", addressService.getAllAddresses()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<AddressResDto>> getAddressById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Address fetched successfully", addressService.getAddressById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<AddressResDto>> addAddress(@RequestBody @Validated(OnCreate.class) AddressReqDto addressReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Address added successfully", addressService.addAddress(addressReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<AddressResDto>> updateAddress(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) AddressReqDto addressReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Address updated successfully", addressService.updateAddress(id, addressReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable UUID id) {
        String res = "Address not deleted";
        if (addressService.deleteAddress(id)){
            res = "Address deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
