package com.hrms.backend.repos;

import com.hrms.backend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepo extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostPkPostIdAndParentCommentIsNullAndIsDeletedFalseOrderByCreatedAtAsc(UUID postId);
}
