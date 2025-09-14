package com.aura.connectcampus.blogs;

import com.aura.connectcampus.blogs.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/admin/blogs") @RequiredArgsConstructor
public class BlogAdminController {
    private final BlogService service;

    @PostMapping
    public ResponseEntity<BlogPostSummaryDto> create(@RequestBody @Valid BlogCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostSummaryDto> update(@PathVariable Long id, @RequestBody @Valid BlogUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        service.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
