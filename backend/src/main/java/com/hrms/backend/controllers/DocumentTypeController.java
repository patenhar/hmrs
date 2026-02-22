package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.DocumentTypeReqDto;
import com.hrms.backend.dtos.response.DocumentTypeResDto;
import com.hrms.backend.entities.DocumentType;
import com.hrms.backend.services.DocumentTypeService;
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
@RequestMapping("api/document-types")
public class DocumentTypeController {
    private final DocumentTypeService documentTypeService;

    @Autowired
    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<DocumentTypeResDto>>> getAllDocumentTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All document types fetched successfully", documentTypeService.getAllDocumentTypes()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DocumentTypeResDto>>> getDocumentTypesByName(@RequestParam String query) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Document types fetched by name", documentTypeService.findDocumentTypeByDocumentTypeName(query)));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<DocumentTypeResDto>> getDocumentTypeById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Document type fetched successfully", documentTypeService.getDocumentTypeById(id)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<DocumentTypeResDto>> addDocumentType(@RequestBody @Validated(OnCreate.class) DocumentTypeReqDto documentTypeReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Document type added successfully", documentTypeService.addDocumentType(documentTypeReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<DocumentTypeResDto>> updateDocumentType(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) DocumentTypeReqDto documentTypeReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Document type updated successfully", documentTypeService.updateDocumentType(id, documentTypeReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteDocumentType(@PathVariable UUID id) {
        String res = "Document type not deleted";
        if (documentTypeService.deleteDocumentType(id)){
            res = "Document type deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
