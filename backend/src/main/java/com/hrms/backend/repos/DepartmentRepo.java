package com.hrms.backend.repos;

import com.hrms.backend.entities.Department;
import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DepartmentRepo extends JpaRepository<Department, UUID>{
    List<Department> findDepartmentsByDepartmentNameContainingIgnoreCase(String name);
}
