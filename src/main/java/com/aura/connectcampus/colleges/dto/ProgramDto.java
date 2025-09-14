package com.aura.connectcampus.colleges.dto;

import java.util.List;

public record ProgramDto(
        Long id,
        String name,
        String degree,
        short durationYears,
        List<CutoffDto> cutoffs
) {}
