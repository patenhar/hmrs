package com.hrms.backend.repos;

import com.hrms.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID>{

}
