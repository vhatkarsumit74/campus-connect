package com.aura.connectcampus.tickets;

import com.aura.connectcampus.colleges.College;
import com.aura.connectcampus.common.TicketStatus;
import com.aura.connectcampus.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact_tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContactTicket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="created_by_user_id")
    private User createdBy;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="college_id")
    private College college;

    @Column(nullable=false) private String subject;
    @Column(nullable=false) @Lob private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TicketStatus status = TicketStatus.OPEN;

    @Column(nullable=false)
    private Instant createdAt = Instant.now();

    @Column(nullable=false)
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContactMessage> messages = new ArrayList<>();

    @PreUpdate void onUpdate() { this.updatedAt = Instant.now(); }
}
