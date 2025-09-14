package com.aura.connectcampus.users.dto;

import com.aura.connectcampus.common.Role;

import java.math.BigDecimal;

public record UserProfileDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Short age,
        BigDecimal marks12th,
        BigDecimal cetPercentile,
        BigDecimal jeePercentile,
        Role role,
        boolean verified
) {}
