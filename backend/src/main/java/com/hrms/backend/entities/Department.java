package com.hrms.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Departments")
@Getter @Setter
public class Department extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pkDepartmentId;

    @NotBlank
    private String departmentName;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private List<Profile> profiles = new ArrayList<>();
}
