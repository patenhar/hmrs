package com.hrms.backend.services;

import com.hrms.backend.dtos.request.ReferralInDto;
import com.hrms.backend.dtos.request.ReferralReqDto;
import com.hrms.backend.dtos.response.ReferralResDto;
import com.hrms.backend.dtos.spec.ReferralSpecDto;
import com.hrms.backend.dtos.spec.SortDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.enums.ReferralStatus;
import com.hrms.backend.repos.ReferralRepo;
import com.hrms.backend.utils.JsonStringToSortDto;
import com.hrms.backend.utils.ReferralSpecification;
import com.hrms.backend.utils.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ReferralService {

    private final JobService jobService;
    private final UserService userService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final ReferralRepo referralRepo;
    private final NotificationService notificationService;

    public ReferralService(JobService jobService, UserService userService, EmailService emailService, ModelMapper modelMapper, DocumentService documentService, ReferralRepo referralRepo, NotificationService notificationService) {
        this.jobService = jobService;
        this.userService = userService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
        this.referralRepo = referralRepo;
        this.notificationService = notificationService;
    }

    public void referJob(UUID jobId, ReferralReqDto referralReqDto) throws IOException, MessagingException {
        Job job = jobService.findJobById(jobId);
        User user = userService.getAuthenticatedUser();
        Document document = documentService.uploadDocument(referralReqDto.getDocumentReqDto());
        for (JobStakeHolder jobStakeHolder: job.getJobStakeHolders()){
            emailService.sendMailWithAttachment(jobStakeHolder.getUser().getEmail(), "Job referral", "Details" , document.getAccessUrl());
        }
        Referral referral = modelMapper.map(referralReqDto, Referral.class);
        referral.setJob(job);
        referral.setUser(user);
        referral.setCv(document);
        referral.setReferralStatus(ReferralStatus.PENDING);
        referralRepo.save(referral);
    }

    public List<ReferralResDto> getAllReferrals() {
        return referralRepo.findAll().stream().map(r -> modelMapper.map(r, ReferralResDto.class)).toList();
    }

    public ReferralResDto updateReferralStatus(UUID referralId, ReferralStatus status) {
        Referral referral = findReferralById(referralId);
        referral.setReferralStatus(status);
        Referral updatedReferral = referralRepo.save(referral);

        if (updatedReferral.getUser() != null) {
            String statusName = status.getDisplayName();
            String title = "Referral Status Updated";
            String description = "Your referral for " + updatedReferral.getName()
                    + " applying to '" + updatedReferral.getJob().getTitle()
                    + "' has been updated to: " + statusName + ".";
            notificationService.createNotification(title, description, updatedReferral.getUser().getPkUserId());
            emailService.sendMail(updatedReferral.getUser().getEmail(), title, description);
        }

        return modelMapper.map(updatedReferral, ReferralResDto.class);
    }

    private String mapSortField(String field) {
        return switch (field) {
            case "statusName"  -> "referralStatus";
            case "jobTitle"   -> "job.title";
            default -> field;
        };
    }

    public Referral findReferralById(UUID id) {
        return referralRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Referral not found"));
    }

    public Page<ReferralResDto> searchReferralsWithPaginationSortingAndFiltering(ReferralInDto referralInDto) {
        ReferralSpecDto specDto = ReferralSpecDto.builder()
                .name(referralInDto.getName())
                .email(referralInDto.getEmail())
                .jobTitle(referralInDto.getJobTitle())
                .status(referralInDto.getStatus())
                .build();

        List<SortDto> sortDtos = JsonStringToSortDto.jsonStringToSortDto(referralInDto.getSort());
        List<Sort.Order> orders = new ArrayList<>();
        if (sortDtos != null) {
            for (SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, mapSortField(sortDto.getField())));
            }
        }

        PageRequest pageRequest = PageRequest.of(
                referralInDto.getPage(),
                referralInDto.getSize(),
                Sort.by(orders)
        );

        Specification<Referral> specification = ReferralSpecification.getSpecification(specDto);
        Page<Referral> referrals = referralRepo.findAll(specification, pageRequest);
        return referrals.map(r -> modelMapper.map(r, ReferralResDto.class));
    }
}
