package com.aura.connectcampus.blogs;

import com.aura.connectcampus.blogs.dto.*;
import com.aura.connectcampus.common.exceptions.ResourceNotFoundException;
import com.aura.connectcampus.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class BlogService {
    private final BlogPostRepository postRepo;
    private final BlogCommentRepository commentRepo;
    private final CurrentUser currentUser;

    public Page<BlogPostSummaryDto> list(Pageable pageable) {
        return postRepo.findAll(pageable).map(BlogMapper::toSummary);
    }

    public BlogPostDetailsDto getBySlug(String slug) {
        BlogPost p = postRepo.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        var comments = commentRepo.findByPostIdOrderByCreatedAtDesc(p.getId());
        return BlogMapper.toDetails(p, comments);
    }

    // ----- Admin -----
    @Transactional
    public BlogPostSummaryDto create(BlogCreateRequest req) {
        String slug = (req.slug() == null || req.slug().isBlank())
                ? toSlug(req.title()) : req.slug().trim().toLowerCase();
        if (postRepo.existsBySlug(slug)) throw new IllegalArgumentException("Slug already exists");

        BlogPost p = BlogPost.builder()
                .title(req.title())
                .slug(slug)
                .summary(req.summary())
                .content(req.content())
                .authorName(req.authorName())
                .build();
        postRepo.save(p);
        return BlogMapper.toSummary(p);
    }

    @Transactional
    public BlogPostSummaryDto update(Long id, BlogUpdateRequest req) {
        BlogPost p = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog not found: " + id));
        if (req.title() != null) p.setTitle(req.title());
        if (req.summary() != null) p.setSummary(req.summary());
        if (req.content() != null) p.setContent(req.content());
        if (req.authorName() != null) p.setAuthorName(req.authorName());
        if (req.slug() != null && !req.slug().isBlank()) {
            String newSlug = req.slug().trim().toLowerCase();
            if (!newSlug.equals(p.getSlug()) && postRepo.existsBySlug(newSlug)) {
                throw new IllegalArgumentException("Slug already exists");
            }
            p.setSlug(newSlug);
        }
        return BlogMapper.toSummary(p);
    }

    public void delete(Long id) {
        if (!postRepo.existsById(id)) throw new ResourceNotFoundException("Blog not found: " + id);
        postRepo.deleteById(id);
    }

    // ----- Comments -----
    @Transactional
    public BlogCommentDto addComment(String slug, BlogCommentCreateRequest req) {
        BlogPost p = postRepo.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        var user = currentUser.require();

        BlogComment c = BlogComment.builder()
                .post(p)
                .user(user)
                .authorName(null) // derive from user
                .body(req.body())
                .build();
        commentRepo.save(c);
        return BlogMapper.toComment(c);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepo.existsById(commentId)) throw new ResourceNotFoundException("Comment not found: " + commentId);
        commentRepo.deleteById(commentId);
    }

    private static String toSlug(String title) {
        return title == null ? null : title.trim().toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}
