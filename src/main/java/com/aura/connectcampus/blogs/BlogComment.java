package com.aura.connectcampus.blogs;

import com.aura.connectcampus.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "blog_comments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BlogComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="post_id", nullable=false)
    private BlogPost post;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id")
    private User user;

    private String authorName;

    @Lob @Column(nullable=false)
    private String body;

    @Column(nullable=false)
    private Instant createdAt = Instant.now();
}
