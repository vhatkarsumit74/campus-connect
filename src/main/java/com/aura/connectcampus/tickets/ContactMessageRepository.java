package com.aura.connectcampus.tickets;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
