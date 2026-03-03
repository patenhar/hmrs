package com.hrms.backend.services;

import com.hrms.backend.dtos.request.PostReqDto;
import com.hrms.backend.dtos.response.CurrentUserResDto;
import com.hrms.backend.dtos.response.CommentResDto;
import com.hrms.backend.dtos.response.PostResDto;
import com.hrms.backend.dtos.response.PostVisibilityResDto;
import com.hrms.backend.dtos.response.TagResDto;
import com.hrms.backend.dtos.response.UserResDto;
import com.hrms.backend.entities.*;
import com.hrms.backend.repos.*;
import com.hrms.backend.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final LikeRepo likeRepo;
    private final CommentRepo commentRepo;
    private final TagService tagService;
    private final PostVisibilityRepo postVisibilityRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    public PostService(PostRepo postRepo, LikeRepo likeRepo, CommentRepo commentRepo,
                       TagService tagService, PostVisibilityRepo postVisibilityRepo,
                       UserService userService, UserRepo userRepo,
                       EmailService emailService, ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
        this.commentRepo = commentRepo;
        this.tagService = tagService;
        this.postVisibilityRepo = postVisibilityRepo;
        this.userService = userService;
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Post findPostEntityById(UUID id) {
        return postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    private PostResDto toResDto(Post post, UUID currentUserId) {
        PostResDto dto = new PostResDto();
        dto.setPkPostId(post.getPkPostId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setIsDeleted(post.getIsDeleted());
        dto.setIsSystemGenerated(post.getIsSystemGenerated());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setVisibility(post.getVisibility() != null
                ? modelMapper.map(post.getVisibility(), PostVisibilityResDto.class)
                : null);
        dto.setAuthor(post.getAuthor() != null
                ? modelMapper.map(post.getAuthor(), UserResDto.class)
                : null);
        dto.setTags(post.getTags().stream()
                .map(t -> modelMapper.map(t, TagResDto.class))
                .toList());

        long likeCount = likeRepo.countByPostPkPostId(post.getPkPostId());
        dto.setLikeCount(likeCount);

        if (currentUserId != null) {
            dto.setLikedByCurrentUser(likeRepo.existsByPostPkPostIdAndUserPkUserId(post.getPkPostId(), currentUserId));
        }

        List<Like> likes = likeRepo.findByPostPkPostId(post.getPkPostId());
        List<UserResDto> recentLikers = likes.stream()
                .sorted((a, b) -> b.getLikedAt().compareTo(a.getLikedAt()))
                .limit(5)
                .map(l -> modelMapper.map(l.getUser(), UserResDto.class))
                .toList();
        dto.setRecentLikers(recentLikers);

        List<Comment> topLevelComments = commentRepo
                .findByPostPkPostIdAndParentCommentIsNullAndIsDeletedFalseOrderByCreatedAtAsc(post.getPkPostId());
        dto.setCommentCount(topLevelComments.size());
        dto.setComments(topLevelComments.stream().map(this::toCommentResDto).toList());

        return dto;
    }

    private CommentResDto toCommentResDto(Comment comment) {
        CommentResDto dto = new CommentResDto();
        dto.setPkCommentId(comment.getPkCommentId());
        dto.setContent(comment.getContent());
        dto.setIsDeleted(comment.getIsDeleted());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setAuthor(comment.getCreatedBy() != null
                ? userService.findUserById(UUID.fromString(comment.getCreatedBy()))
                : null);
        List<CommentResDto> replies = comment.getReplies().stream()
                .filter(r -> !r.getIsDeleted())
                .map(this::toCommentResDto)
                .toList();
        dto.setReplies(replies);
        return dto;
    }

    // ── public API ────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<PostResDto> getAllPosts(String authorId, String tag, LocalDate from, LocalDate to) {
        UUID currentUserId = null;
        try {
            currentUserId = UUID.fromString(userService.getCurrentUser().getPkUserId().toString());
        } catch (Exception ignored) { /* anonymous call */ }

        LocalDateTime fromDt = from != null ? from.atStartOfDay() : null;
        LocalDateTime toDt   = to   != null ? to.atTime(LocalTime.MAX) : null;

        List<Post> posts = postRepo.findFilteredPosts(authorId, tag, fromDt, toDt);

        final UUID finalCurrentUserId = currentUserId;
        return posts.stream().map(p -> toResDto(p, finalCurrentUserId)).toList();
    }

    @Transactional(readOnly = true)
    public PostResDto getPostById(UUID id) {
        Post post = findPostEntityById(id);
        UUID currentUserId = null;
        try {
            currentUserId = userService.getCurrentUser().getPkUserId();
        } catch (Exception ignored) { }
        return toResDto(post, currentUserId);
    }

    @Transactional
    public PostResDto createPost(PostReqDto dto) {
        CurrentUserResDto currentUser = userService.getCurrentUser();
        User author = userRepo.findById(currentUser.getPkUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PostVisibility visibility = postVisibilityRepo.findById(dto.getVisibilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Visibility not found"));

        List<Tag> tags = new ArrayList<>();
        if (dto.getTags() != null) {
            for (String tagName : dto.getTags()) {
                if (tagName != null && !tagName.isBlank()) {
                    tags.add(tagService.findOrCreateByName(tagName));
                }
            }
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setAuthor(author);
        post.setVisibility(visibility);
        post.setTags(tags);
        post.setIsDeleted(false);
        post.setIsSystemGenerated(false);

        Post saved = postRepo.save(post);
        return toResDto(saved, currentUser.getPkUserId());
    }

    @Transactional
    public PostResDto updatePost(UUID id, PostReqDto dto) {
        Post post = findPostEntityById(id);
        CurrentUserResDto currentUser = userService.getCurrentUser();

        if (post.getAuthor() == null || !post.getAuthor().getPkUserId().equals(currentUser.getPkUserId())) {
            throw new org.springframework.security.access.AccessDeniedException("You can only edit your own posts");
        }

        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());

        if (dto.getTags() != null) {
            List<Tag> tags = new ArrayList<>(dto.getTags().stream()
                    .filter(n -> n != null && !n.isBlank())
                    .map(tagService::findOrCreateByName)
                    .toList());
            post.setTags(tags);
        }

        Post saved = postRepo.save(post);
        return toResDto(saved, currentUser.getPkUserId());
    }

    @Transactional
    public boolean softDeletePost(UUID id, String remarks, boolean isHrAction) {
        Post post = findPostEntityById(id);
        CurrentUserResDto currentUser = userService.getCurrentUser();

        post.setIsDeleted(true);
        post.setRemarks(remarks);
        post.setDeletedBy(currentUser.getPkUserId());
        postRepo.save(post);

        if (isHrAction && post.getAuthor() != null) {
            String authorEmail = post.getAuthor().getEmail();
            String authorName  = post.getAuthor().getEmail(); // fallback; swap for name if profile available
            String subject = "[HRMS Notice] Your post has been removed";
            String body = "Dear " + authorName + ",\n\n"
                    + "This is to inform you that a post you published on the HRMS Achievements feed "
                    + "has been removed by HR for the following reason:\n\n"
                    + "\"" + remarks + "\"\n\n"
                    + "Please ensure future content adheres to company guidelines.\n"
                    + "If you believe this was an error, contact HR.\n\n"
                    + "Regards,\nHRMS Platform";
            try {
                emailService.sendMail(authorEmail, subject, body);
            } catch (Exception ignored) { /* log in production */ }
        }

        return true;
    }

    @Transactional
    public Post createSystemPost(UUID subjectUserId, String title, String description, List<UUID> tagIds) {
        PostVisibility visibility = postVisibilityRepo
                .findByVisibilityIgnoreCase("ALL_EMPLOYEES")
                .orElseGet(() -> {
                    PostVisibility pv = new PostVisibility();
                    pv.setVisibility("ALL_EMPLOYEES");
                    return postVisibilityRepo.save(pv);
                });

        User subjectUser = userRepo.findById(subjectUserId).orElse(null);

        List<Tag> tags = new ArrayList<>();
        if (tagIds != null) {
            for (UUID tagId : tagIds) {
                try { tags.add(tagService.findTagEntityById(tagId)); }
                catch (Exception ignored) { }
            }
        }

        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setAuthor(subjectUser);
        post.setVisibility(visibility);
        post.setTags(tags);
        post.setIsDeleted(false);
        post.setIsSystemGenerated(true);

        return postRepo.save(post);
    }

    @Transactional
    public boolean toggleLike(UUID postId) {
        Post post = findPostEntityById(postId);
        CurrentUserResDto currentUser = userService.getCurrentUser();
        UUID userId = currentUser.getPkUserId();

        if (likeRepo.existsByPostPkPostIdAndUserPkUserId(postId, userId)) {
            likeRepo.deleteByPostPkPostIdAndUserPkUserId(postId, userId);
            return false; // unliked
        } else {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepo.save(like);
            return true; // liked
        }
    }
}
