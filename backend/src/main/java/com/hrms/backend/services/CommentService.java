package com.hrms.backend.services;

import com.hrms.backend.dtos.request.CommentReqDto;
import com.hrms.backend.dtos.response.CurrentUserResDto;
import com.hrms.backend.dtos.response.CommentResDto;
import com.hrms.backend.dtos.response.UserResDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.CommentRepo;
import com.hrms.backend.repos.PostRepo;
import com.hrms.backend.repos.UserRepo;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepo commentRepo, PostRepo postRepo, UserRepo userRepo,
                          UserService userService, EmailService emailService,
                          ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.userService = userService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    private CommentResDto toResDto(Comment comment) {
        CommentResDto dto = new CommentResDto();
        dto.setPkCommentId(comment.getPkCommentId());
        dto.setContent(comment.getContent());
        dto.setIsDeleted(comment.getIsDeleted());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());

        if (comment.getCreatedBy() != null) {
            try {
                dto.setAuthor(userService.findUserById(UUID.fromString(comment.getCreatedBy())));
            } catch (Exception ignored) { }
        }

        List<CommentResDto> replies = comment.getReplies().stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .map(this::toResDto)
                .toList();
        dto.setReplies(replies);
        return dto;
    }

    public List<CommentResDto> getCommentsByPostId(UUID postId) {
        return commentRepo
                .findByPostPkPostIdAndParentCommentIsNullAndIsDeletedFalseOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toResDto)
                .toList();
    }

    @Transactional
    public CommentResDto addComment(CommentReqDto dto) {
        Post post = postRepo.findById(dto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (Boolean.TRUE.equals(post.getIsDeleted())) {
            throw new ResourceNotFoundException("Cannot comment on a deleted post");
        }

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setPost(post);
        comment.setIsDeleted(false);

        if (dto.getParentCommentId() != null) {
            Comment parent = commentRepo.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            comment.setParentComment(parent);
        }

        Comment saved = commentRepo.save(comment);
        return toResDto(saved);
    }

    @Transactional
    public CommentResDto updateComment(UUID id, CommentReqDto dto) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        CurrentUserResDto currentUser = userService.getCurrentUser();
        if (comment.getCreatedBy() == null
                || !comment.getCreatedBy().equals(currentUser.getPkUserId().toString())) {
            throw new AccessDeniedException("You can only edit your own comments");
        }

        comment.setContent(dto.getContent());
        Comment saved = commentRepo.save(comment);
        return toResDto(saved);
    }

    @Transactional
    public boolean softDeleteComment(UUID id, String remarks, boolean isHrAction) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        CurrentUserResDto currentUser = userService.getCurrentUser();
        comment.setIsDeleted(true);
        comment.setRemarks(remarks);
        comment.setDeletedBy(currentUser.getPkUserId());
        commentRepo.save(comment);

        if (isHrAction && comment.getCreatedBy() != null) {
            try {
                UserResDto author = userService.findUserById(UUID.fromString(comment.getCreatedBy()));
                String subject = "[HRMS Notice] Your comment has been removed";
                String body = "Dear " + author.getEmail() + ",\n\n"
                        + "This is to inform you that a comment you published on the HRMS Achievements feed "
                        + "has been removed by HR for the following reason:\n\n"
                        + "\"" + remarks + "\"\n\n"
                        + "Please ensure future content adheres to company guidelines.\n"
                        + "If you believe this was an error, contact HR.\n\n"
                        + "Regards,\nHRMS Platform";
                emailService.sendMail(author.getEmail(), subject, body);
            } catch (Exception ignored) { }
        }

        return true;
    }
}
