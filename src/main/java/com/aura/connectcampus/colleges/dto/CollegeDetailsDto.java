package com.aura.connectcampus.colleges.dto;

import java.util.List;

public record CollegeDetailsDto(
        Long id,
        String name,
        String city,
        String state,
        Integer fees,
        String website,
        String phone,
        List<ProgramDto> programs
) {}
