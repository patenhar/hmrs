package com.hrms.backend.repos;

import com.hrms.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID>{
    Optional<Role> findFirstByRoleName(String roleName);
    List<Role> findAllRolesByRoleNameContainingIgnoreCase(String roleName);
}
