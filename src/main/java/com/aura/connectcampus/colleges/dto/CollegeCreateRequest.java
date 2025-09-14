package com.aura.connectcampus.colleges.dto;

import jakarta.validation.constraints.NotBlank;

public record CollegeCreateRequest(
        @NotBlank String name,
        String city,
        String state,
        Integer fees,
        String website,
        String phone
) {}
