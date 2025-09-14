package com.aura.connectcampus.colleges.dto;

import com.aura.connectcampus.common.Exam;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CutoffRequest(
        @NotNull Exam exam,
        @Positive short year,
        @NotNull BigDecimal percentileCutoff
) {}
