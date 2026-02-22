package com.hrms.backend.dtos.response;

import com.hrms.backend.entities.Profile;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProfileStatusResDto {
    private UUID pkProfileStatusId;
    private String profileStatusName;
}
