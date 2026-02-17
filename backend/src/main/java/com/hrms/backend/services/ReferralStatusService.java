package com.hrms.backend.services;

import com.hrms.backend.dtos.response.ReferralStatusResDto;
import com.hrms.backend.entities.Referral;
import com.hrms.backend.entities.ReferralStatus;
import com.hrms.backend.repos.ReferralStatusRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReferralStatusService {

    private final ReferralStatusRepo referralStatusRepo;
    private final ModelMapper modelMapper;

    public ReferralStatusService(ReferralStatusRepo referralStatusRepo, ModelMapper modelMapper) {
        this.referralStatusRepo = referralStatusRepo;
        this.modelMapper = modelMapper;
    }

    public ReferralStatusResDto findReferralStatusById(UUID id) {
        ReferralStatus referralStatus = referralStatusRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Referral status not found"));
        return modelMapper.map(referralStatus, ReferralStatusResDto.class);
    }
}
