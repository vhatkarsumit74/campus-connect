package com.aura.connectcampus.tickets.dto;

import com.aura.connectcampus.common.TicketStatus;

import java.time.Instant;

public record TicketSummaryDto(Long id, String subject, TicketStatus status, Instant createdAt,
                               Long collegeId, String collegeName) {}
