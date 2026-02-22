package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "fk_document_type_id", referencedColumnName = "pkDocumentTypeId")
    private DocumentType documentType;
}
