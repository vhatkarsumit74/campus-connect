package com.aura.connectcampus.bookmarks;

import com.aura.connectcampus.colleges.College;
import com.aura.connectcampus.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "bookmarks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bookmark {
    @EmbeddedId
    private BookmarkId id;

    @ManyToOne(fetch = FetchType.LAZY) @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @MapsId("collegeId")
    @JoinColumn(name = "college_id")
    private College college;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
