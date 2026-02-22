package com.hrms.backend.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DocumentResDto {
    private UUID pkDocumentId;
    private String accessUrl;
    private DocumentTypeResDto documentType;
}
