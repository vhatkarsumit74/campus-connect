package com.aura.connectcampus.tickets;

import com.aura.connectcampus.tickets.dto.*;

import java.util.List;

public final class TicketMapper {
    private TicketMapper() {}

    public static TicketSummaryDto toSummary(ContactTicket t) {
        return new TicketSummaryDto(
                t.getId(), t.getSubject(), t.getStatus(), t.getCreatedAt(),
                t.getCollege() == null ? null : t.getCollege().getId(),
                t.getCollege() == null ? null : t.getCollege().getName()
        );
    }

    public static TicketDetailsDto toDetails(ContactTicket t, List<ContactMessage> msgs) {
        List<MessageDto> list = msgs.stream().map(TicketMapper::toMessage).toList();
        return new TicketDetailsDto(
                t.getId(), t.getSubject(), t.getDescription(), t.getStatus(),
                t.getCreatedAt(), t.getUpdatedAt(),
                t.getCollege() == null ? null : t.getCollege().getId(),
                t.getCollege() == null ? null : t.getCollege().getName(),
                list
        );
    }

    public static MessageDto toMessage(ContactMessage m) {
        String author = m.getAuthor() == null ? "Admin" :
                (m.getAuthor().getFirstName() != null ? m.getAuthor().getFirstName() : m.getAuthor().getEmail());
        return new MessageDto(m.getId(), author, m.getBody(), m.getCreatedAt());
    }
}
