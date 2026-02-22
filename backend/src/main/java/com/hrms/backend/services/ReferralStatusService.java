package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ReferralStatusReqDto;
import com.hrms.backend.dtos.response.ReferralStatusResDto;
import com.hrms.backend.entities.ReferralStatus;
import com.hrms.backend.repos.ReferralStatusRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<ReferralStatusResDto> getAllReferralStatuses() {
        return referralStatusRepo.findAll().stream().map(st -> modelMapper.map(st, ReferralStatusResDto.class)).toList();
    }

    public ReferralStatusResDto getReferralStatusById(UUID id) {
        return findReferralStatusById(id);
    }

    public ReferralStatusResDto addReferralStatus(ReferralStatusReqDto referralStatusReqDto) {
        ReferralStatus referralStatus = referralStatusRepo.save(modelMapper.map(referralStatusReqDto, ReferralStatus.class));
        return modelMapper.map(referralStatus, ReferralStatusResDto.class);
    }

    public ReferralStatusResDto updateReferralStatus(UUID id, ReferralStatusReqDto referralStatusReqDto) {
        ReferralStatusResDto referralStatus = findReferralStatusById(id);
        modelMapper.map(referralStatusReqDto, referralStatus);
        ReferralStatus updatedReferralStatus = referralStatusRepo.save(modelMapper.map(referralStatusReqDto, ReferralStatus.class));
        return modelMapper.map(updatedReferralStatus, ReferralStatusResDto.class);
    }

    public boolean deleteReferralStatus(UUID id) {
        findReferralStatusById(id);
        referralStatusRepo.deleteById(id);
        return true;
    }
}
