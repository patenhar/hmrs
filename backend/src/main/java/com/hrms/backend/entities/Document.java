package com.hrms.backend.entities;

import com.hrms.backend.enums.DocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Documents")
@Getter @Setter
public class Document extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkDocumentId;

    @NotBlank
    private String accessUrl;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
}
