package com.aura.connectcampus.colleges;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colleges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class College {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    private String city;
    private String state;
    private Integer fees;       // total/yearly as per your UI
    private String website;
    private String phone;

    // NEW: optional email used for contact notifications
    private String email;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Program> programs = new ArrayList<>();

    @Column(nullable=false)
    private Instant createdAt = Instant.now();

    @Column(nullable=false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void onUpdate() { this.updatedAt = Instant.now(); }

    public void addProgram(Program p) {
        programs.add(p);
        p.setCollege(this);
    }
}
