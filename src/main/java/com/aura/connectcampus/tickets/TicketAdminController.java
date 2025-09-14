package com.aura.connectcampus.tickets;

import com.aura.connectcampus.common.TicketStatus;
import com.aura.connectcampus.tickets.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/admin/tickets") @RequiredArgsConstructor
public class TicketAdminController {
    private final TicketService service;

    @GetMapping
    public Page<TicketSummaryDto> list(@RequestParam(required = false) TicketStatus status, Pageable pageable) {
        return service.listAll(status, pageable);
    }

    @GetMapping("/{id}")
    public TicketDetailsDto get(@PathVariable Long id) {
        return service.getAny(id);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageDto> addMessage(@PathVariable Long id, @RequestBody @Valid MessageCreateRequest req) {
        return ResponseEntity.ok(service.addMessageAdmin(id, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketSummaryDto> updateStatus(@PathVariable Long id, @RequestBody @Valid TicketStatusUpdateRequest req) {
        return ResponseEntity.ok(service.updateStatus(id, req));
    }
}
