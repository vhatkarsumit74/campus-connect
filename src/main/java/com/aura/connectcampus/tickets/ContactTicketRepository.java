package com.aura.connectcampus.tickets;

import com.aura.connectcampus.common.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactTicketRepository extends JpaRepository<ContactTicket, Long> {
    Page<ContactTicket> findByCreatedById(Long userId, Pageable pageable);
    Page<ContactTicket> findByStatus(TicketStatus status, Pageable pageable);
}
