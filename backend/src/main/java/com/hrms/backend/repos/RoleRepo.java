package com.hrms.backend.repos;

import com.hrms.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
    Optional<Role> findFirstByRoleName(String roleName);
    List<Role> findAllRolesByRoleNameContainingIgnoreCase(String roleName);
}
