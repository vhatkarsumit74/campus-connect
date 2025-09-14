package com.aura.connectcampus.colleges;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "programs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Program {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Column(nullable=false)
    private String name;     // e.g., "CSE", "IT", "Mechanical"

    @Column(nullable=false)
    private String degree;   // e.g., "BE", "BTech"

    @Column(nullable=false)
    private short durationYears;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Cutoff> cutoffs = new ArrayList<>();

    public void addCutoff(Cutoff c) {
        cutoffs.add(c);
        c.setProgram(this);
    }
}
