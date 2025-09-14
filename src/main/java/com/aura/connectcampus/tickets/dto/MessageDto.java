package com.aura.connectcampus.tickets.dto;

import java.time.Instant;

public record MessageDto(Long id, String authorName, String body, Instant createdAt) {}

