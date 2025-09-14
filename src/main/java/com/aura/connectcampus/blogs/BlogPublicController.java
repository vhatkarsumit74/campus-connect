package com.aura.connectcampus.blogs;

import com.aura.connectcampus.blogs.dto.BlogCommentCreateRequest;
import com.aura.connectcampus.blogs.dto.BlogPostDetailsDto;
import com.aura.connectcampus.blogs.dto.BlogPostSummaryDto;
import com.aura.connectcampus.blogs.dto.BlogCommentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/blogs") @RequiredArgsConstructor
public class BlogPublicController {
    private final BlogService service;

    @GetMapping
    public Page<BlogPostSummaryDto> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{slug}")
    public BlogPostDetailsDto get(@PathVariable String slug) {
        return service.getBySlug(slug);
    }

    // Student-authenticated can comment
    @PostMapping("/{slug}/comments")
    public ResponseEntity<BlogCommentDto> comment(@PathVariable String slug, @RequestBody @Valid BlogCommentCreateRequest req) {
        return ResponseEntity.ok(service.addComment(slug, req));
    }
}
