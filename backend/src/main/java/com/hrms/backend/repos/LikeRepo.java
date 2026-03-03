package com.hrms.backend.repos;

import com.hrms.backend.entities.Like;
import com.hrms.backend.entities.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepo extends JpaRepository<Like, LikeId> {
    List<Like> findByPostPkPostId(UUID postId);
    long countByPostPkPostId(UUID postId);
    Optional<Like> findByPostPkPostIdAndUserPkUserId(UUID postId, UUID userId);
    boolean existsByPostPkPostIdAndUserPkUserId(UUID postId, UUID userId);
    void deleteByPostPkPostIdAndUserPkUserId(UUID postId, UUID userId);
    void deleteAllByUser_PkUserId(UUID userId);
}
