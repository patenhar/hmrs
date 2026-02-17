package com.hrms.backend.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DocumentTypeResDto {
    private UUID pkDocumentTypeId;
    private String documentTypeName;
}
