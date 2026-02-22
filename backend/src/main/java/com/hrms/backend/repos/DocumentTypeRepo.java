package com.hrms.backend.repos;

import com.hrms.backend.entities.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentTypeRepo extends JpaRepository<DocumentType, UUID>{
    List<DocumentType> findDocumentTypeByDocumentTypeNameContainingIgnoreCase(String name);
}
