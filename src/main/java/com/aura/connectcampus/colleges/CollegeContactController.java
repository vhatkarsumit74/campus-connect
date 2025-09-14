package com.aura.connectcampus.colleges;

import com.aura.connectcampus.colleges.dto.ContactCollegeRequest;
import com.aura.connectcampus.common.MailService;
import com.aura.connectcampus.common.exceptions.ResourceNotFoundException;
import com.aura.connectcampus.security.CurrentUser;
import com.aura.connectcampus.tickets.TicketService;
import com.aura.connectcampus.tickets.dto.TicketCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colleges")
@RequiredArgsConstructor
public class CollegeContactController {

    private final CollegeRepository collegeRepo;
    private final CurrentUser currentUser;
    private final MailService mail;
    private final TicketService ticketService;

    // Optional fallback admin email to receive a copy if college has no email
    @Value("${app.admin.email:}")
    private String adminEmail;

    @PostMapping("/{collegeId}/contact")
    public ResponseEntity<Void> contactCollege(@PathVariable Long collegeId,
                                               @RequestBody @Valid ContactCollegeRequest req) {
        var user = currentUser.require();
        var college = collegeRepo.findById(collegeId)
                .orElseThrow(() -> new ResourceNotFoundException("College not found: " + collegeId));

        // 1) Create a ticket tied to this college (so Admin can track/triage)
        ticketService.create(new TicketCreateRequest(req.subject(), req.message(), collegeId));

        // 2) Optional: send an email notification to the college (if email present)
        if (college.getEmail() != null && !college.getEmail().isBlank()) {
            String body = """
                    Hello %s,
                    
                    A student reached out from ConnectCampus.
                    
                    From: %s
                    Subject: %s
                    
                    Message:
                    %s
                    """.formatted(college.getName(), user.getEmail(), req.subject(), req.message());
            mail.send(college.getEmail(), "[ConnectCampus] Student enquiry: " + req.subject(), body);
        } else if (adminEmail != null && !adminEmail.isBlank()) {
            // Fallback copy to admin if college email not configured
            String body = """
                    College has no configured email. Forward manually if needed.
                    
                    College: %s (id=%d)
                    From: %s
                    Subject: %s
                    
                    Message:
                    %s
                    """.formatted(college.getName(), college.getId(), user.getEmail(), req.subject(), req.message());
            mail.send(adminEmail, "[ConnectCampus] College contact (no email): " + req.subject(), body);
        }

        return ResponseEntity.ok().build();
    }
}
