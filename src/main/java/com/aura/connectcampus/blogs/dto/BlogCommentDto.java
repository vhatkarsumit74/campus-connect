package com.aura.connectcampus.blogs.dto;

import java.time.Instant;

public record BlogCommentDto(Long id, String authorName, String body, Instant createdAt) {}

