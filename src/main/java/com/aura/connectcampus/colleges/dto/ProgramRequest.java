package com.aura.connectcampus.colleges.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProgramRequest(
        @NotBlank String name,
        @NotBlank String degree,
        @Positive short durationYears
) {}
