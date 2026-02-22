package com.hrms.backend.services;

import com.hrms.backend.entities.Profile;
import com.hrms.backend.entities.Tag;
import com.hrms.backend.repos.ProfileRepo;
import com.hrms.backend.repos.TagRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CelebrationSchedulerService {

    private final ProfileRepo profileRepo;
    private final PostService postService;
    private final TagRepo tagRepo;

    public CelebrationSchedulerService(ProfileRepo profileRepo, PostService postService, TagRepo tagRepo) {
        this.profileRepo = profileRepo;
        this.postService = postService;
        this.tagRepo = tagRepo;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void generateCelebrationPosts() {
        LocalDate today = LocalDate.now();

        UUID birthdayTagId = tagRepo.findByTagIgnoreCase("Birthday")
                .map(Tag::getPkTagId).orElse(null);
        UUID anniversaryTagId = tagRepo.findByTagIgnoreCase("Work Anniversary")
                .map(Tag::getPkTagId).orElse(null);

        List<Profile> profiles = profileRepo.findAll();

        for (Profile profile : profiles) {
            // Birthday check — same month+day, any year
            if (profile.getBirthDate() != null
                    && profile.getBirthDate().getMonth() == today.getMonth()
                    && profile.getBirthDate().getDayOfMonth() == today.getDayOfMonth()) {

                String title = "Happy Birthday, " + profile.getName() + "!";
                String description = "Today is " + profile.getName()
                        + "'s birthday! Wishing them a wonderful day. \uD83C\uDF82";

                List<UUID> tags = birthdayTagId != null ? List.of(birthdayTagId) : List.of();

                try {
                    postService.createSystemPost(
                            profile.getUser().getPkUserId(), title, description, tags);
                    log.info("Birthday post created for {}", profile.getName());
                } catch (Exception e) {
                    log.error("Failed to create birthday post for {}: {}", profile.getName(), e.getMessage());
                }
            }

            // Work anniversary — same month+day but different year
            if (profile.getJoiningDate() != null
                    && profile.getJoiningDate().getMonth() == today.getMonth()
                    && profile.getJoiningDate().getDayOfMonth() == today.getDayOfMonth()
                    && profile.getJoiningDate().getYear() != today.getYear()) {

                int years = today.getYear() - profile.getJoiningDate().getYear();
                String title = "Work Anniversary: " + profile.getName();
                String description = profile.getName() + " completes " + years
                        + (years == 1 ? " year" : " years")
                        + " at the organization. Congratulations! \uD83C\uDFC6";

                List<UUID> tags = anniversaryTagId != null ? List.of(anniversaryTagId) : List.of();

                try {
                    postService.createSystemPost(
                            profile.getUser().getPkUserId(), title, description, tags);
                    log.info("Anniversary post created for {} ({} years)", profile.getName(), years);
                } catch (Exception e) {
                    log.error("Failed to create anniversary post for {}: {}", profile.getName(), e.getMessage());
                }
            }
        }
    }
}
