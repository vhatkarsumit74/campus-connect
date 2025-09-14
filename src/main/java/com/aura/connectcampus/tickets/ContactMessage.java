package com.aura.connectcampus.tickets;

import com.aura.connectcampus.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "contact_messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContactMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ticket_id", nullable=false)
    private ContactTicket ticket;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="author_user_id")
    private User author;

    @Lob @Column(nullable=false)
    private String body;

    @Column(nullable=false)
    private Instant createdAt = Instant.now();
}
