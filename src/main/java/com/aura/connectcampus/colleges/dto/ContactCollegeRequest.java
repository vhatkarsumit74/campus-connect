package com.aura.connectcampus.colleges.dto;

import jakarta.validation.constraints.NotBlank;

public record ContactCollegeRequest(
        @NotBlank String subject,
        @NotBlank String message
) {}
