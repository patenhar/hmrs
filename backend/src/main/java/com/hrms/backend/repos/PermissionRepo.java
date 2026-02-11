package com.hrms.backend.repos;

import com.hrms.backend.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PermissionRepo extends JpaRepository<Permission, UUID>{

}
