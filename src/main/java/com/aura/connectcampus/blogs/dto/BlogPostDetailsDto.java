package com.aura.connectcampus.blogs.dto;

import java.time.Instant;
import java.util.List;

public record BlogPostDetailsDto(Long id, String title, String slug, String summary, String content,
                                 String authorName, Instant createdAt, Instant updatedAt,
                                 List<BlogCommentDto> comments) {}

