package com.hrms.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "Departments")
@Getter @Setter
@JsonIgnoreProperties(value = "roles")
public class Department extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkDepartmentId;

    @NotBlank
    private String departmentName;
}
