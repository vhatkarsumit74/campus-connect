package com.aura.connectcampus.tickets;

import com.aura.connectcampus.colleges.College;
import com.aura.connectcampus.colleges.CollegeRepository;
import com.aura.connectcampus.common.TicketStatus;
import com.aura.connectcampus.common.exceptions.ForbiddenException;
import com.aura.connectcampus.common.exceptions.ResourceNotFoundException;
import com.aura.connectcampus.security.CurrentUser;
import com.aura.connectcampus.tickets.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class TicketService {
    private final ContactTicketRepository ticketRepo;
    private final ContactMessageRepository msgRepo;
    private final CollegeRepository collegeRepo;
    private final CurrentUser currentUser;

    // ---- Student ----
    @Transactional
    public TicketSummaryDto create(TicketCreateRequest req) {
        var user = currentUser.require();

        College college = null;
        if (req.collegeId() != null) {
            college = collegeRepo.findById(req.collegeId())
                    .orElseThrow(() -> new ResourceNotFoundException("College not found: " + req.collegeId()));
        }

        ContactTicket t = ContactTicket.builder()
                .createdBy(user)
                .college(college)
                .subject(req.subject())
                .description(req.description())
                .status(TicketStatus.OPEN)
                .build();
        ticketRepo.save(t);

        // initial message copy of description (optional)
        ContactMessage m = ContactMessage.builder()
                .ticket(t)
                .author(user)
                .body(req.description())
                .build();
        msgRepo.save(m);

        return TicketMapper.toSummary(t);
    }

    public Page<TicketSummaryDto> listMine(Pageable pageable) {
        var user = currentUser.require();
        return ticketRepo.findByCreatedById(user.getId(), pageable).map(TicketMapper::toSummary);
    }

    public TicketDetailsDto getMine(Long id) {
        var user = currentUser.require();
        ContactTicket t = ticketRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (t.getCreatedBy() == null || !t.getCreatedBy().getId().equals(user.getId())) {
            throw new ForbiddenException("Not your ticket");
        }
        var msgs = msgRepo.findByTicketIdOrderByCreatedAtAsc(id);
        return TicketMapper.toDetails(t, msgs);
    }

    @Transactional
    public MessageDto addMessageMine(Long id, MessageCreateRequest req) {
        var user = currentUser.require();
        ContactTicket t = ticketRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (t.getCreatedBy() == null || !t.getCreatedBy().getId().equals(user.getId())) {
            throw new ForbiddenException("Not your ticket");
        }
        ContactMessage m = ContactMessage.builder()
                .ticket(t)
                .author(user)
                .body(req.body())
                .build();
        msgRepo.save(m);
        return TicketMapper.toMessage(m);
    }

    // ---- Admin ----
    public Page<TicketSummaryDto> listAll(TicketStatus status, Pageable pageable) {
        if (status == null) return ticketRepo.findAll(pageable).map(TicketMapper::toSummary);
        return ticketRepo.findByStatus(status, pageable).map(TicketMapper::toSummary);
    }

    public TicketDetailsDto getAny(Long id) {
        ContactTicket t = ticketRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        var msgs = msgRepo.findByTicketIdOrderByCreatedAtAsc(id);
        return TicketMapper.toDetails(t, msgs);
    }

    @Transactional
    public MessageDto addMessageAdmin(Long id, MessageCreateRequest req) {
        var admin = currentUser.require(); // already ADMIN by route
        ContactTicket t = ticketRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ContactMessage m = ContactMessage.builder()
                .ticket(t)
                .author(admin)
                .body(req.body())
                .build();
        msgRepo.save(m);
        return TicketMapper.toMessage(m);
    }

    @Transactional
    public TicketSummaryDto updateStatus(Long id, TicketStatusUpdateRequest req) {
        ContactTicket t = ticketRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        t.setStatus(req.status());
        return TicketMapper.toSummary(t);
    }
}
