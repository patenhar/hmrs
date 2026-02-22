package com.hrms.backend.repos;

import com.hrms.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepo extends JpaRepository<Post, UUID> {
    List<Post> findByIsDeletedFalseOrderByCreatedAtDesc();

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false " +
           "AND (:authorId IS NULL OR CAST(p.author.pkUserId AS string) = :authorId) " +
           "AND (:tag IS NULL OR EXISTS (SELECT t FROM p.tags t WHERE LOWER(t.tag) LIKE LOWER(CONCAT('%', :tag, '%')))) " +
           "AND (:from IS NULL OR p.createdAt >= :from) " +
           "AND (:to IS NULL OR p.createdAt <= :to) " +
           "ORDER BY p.createdAt DESC")
    List<Post> findFilteredPosts(
            @Param("authorId") String authorId,
            @Param("tag") String tag,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
