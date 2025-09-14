package com.aura.connectcampus.users.dto;

import java.math.BigDecimal;

public record UserProfileUpdateRequest(
        String firstName,
        String lastName,
        Short age,
        BigDecimal marks12th,
        BigDecimal cetPercentile,
        BigDecimal jeePercentile
) {}
