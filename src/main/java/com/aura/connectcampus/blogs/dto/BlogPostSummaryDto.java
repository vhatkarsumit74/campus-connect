package com.aura.connectcampus.blogs.dto;

import java.time.Instant;
import java.util.List;

public record BlogPostSummaryDto(Long id, String title, String slug, String summary, Instant createdAt) {}

