package com.aura.connectcampus.tickets.dto;

import com.aura.connectcampus.common.TicketStatus;

import java.time.Instant;
import java.util.List;

public record TicketDetailsDto(Long id, String subject, String description, TicketStatus status,
                               Instant createdAt, Instant updatedAt,
                               Long collegeId, String collegeName,
                               List<MessageDto> messages) {}
