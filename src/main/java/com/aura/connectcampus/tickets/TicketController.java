package com.aura.connectcampus.tickets;

import com.aura.connectcampus.tickets.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/tickets") @RequiredArgsConstructor
public class TicketController {
    private final TicketService service;

    @PostMapping
    public ResponseEntity<TicketSummaryDto> create(@RequestBody @Valid TicketCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping
    public Page<TicketSummaryDto> myTickets(Pageable pageable) {
        return service.listMine(pageable);
    }

    @GetMapping("/{id}")
    public TicketDetailsDto myTicket(@PathVariable Long id) {
        return service.getMine(id);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageDto> addMessage(@PathVariable Long id, @RequestBody @Valid MessageCreateRequest req) {
        return ResponseEntity.ok(service.addMessageMine(id, req));
    }
}
