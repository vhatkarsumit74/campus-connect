package com.aura.connectcampus.colleges;

import com.aura.connectcampus.common.Exam;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cutoffs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cutoff {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Exam exam;   // CET or JEE

    @Column(nullable=false)
    private short year;

    @Column(nullable=false, precision = 5, scale = 2)
    private BigDecimal percentileCutoff; // e.g., 92.50
}
