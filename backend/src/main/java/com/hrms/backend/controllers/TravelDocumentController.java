package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.TravelDocumentReqDto;
import com.hrms.backend.dtos.response.DocumentResDto;
import com.hrms.backend.dtos.response.TravelDocumentResDto;
import com.hrms.backend.entities.TravelDocument;
import com.hrms.backend.services.TravelDocumentService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/travels/users")
public class TravelDocumentController {
    private final TravelDocumentService travelDocumentService;

    @Autowired
    public TravelDocumentController(TravelDocumentService travelDocumentService) {
        this.travelDocumentService = travelDocumentService;
    }

    @GetMapping("/{userTravelId}/documents")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<TravelDocumentResDto>>> getAllTravelDocuments(@PathVariable UUID userTravelId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All travel documents fetched successfully", travelDocumentService.getUserTravelDocument(userTravelId)));
    }

    @PostMapping(value = "/{userTravelId}/documents/upload", consumes = "multipart/form-data")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<TravelDocumentResDto>> addTravelDocument(@PathVariable UUID userTravelId, @Validated(OnCreate.class) @ModelAttribute TravelDocumentReqDto travelDocumentReqDto) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Travel document uploaded successfully", travelDocumentService.uploadTravelDocument(travelDocumentReqDto)));
    }

    @DeleteMapping("/{userTravelId}/documents")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteTravelDocument(@PathVariable UUID userTravelId) {
        travelDocumentService.deleteTravelDocument(userTravelId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("TravelDocument deleted successfully", null ));
    }
}
