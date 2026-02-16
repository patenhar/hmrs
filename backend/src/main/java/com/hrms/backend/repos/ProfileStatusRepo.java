package com.hrms.backend.repos;

import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.ProfileStatus;
import com.hrms.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProfileStatusRepo extends JpaRepository<ProfileStatus, UUID>{

}
