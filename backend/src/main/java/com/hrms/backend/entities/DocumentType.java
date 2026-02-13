package com.hrms.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Document_types")
@Getter @Setter
public class DocumentType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkDocumentTypeId;

    @NotBlank
    private String documentTypeName;

    @OneToMany(mappedBy = "documentType")
    private List<Document> documents = new ArrayList<>();
}
