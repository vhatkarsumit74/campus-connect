package com.aura.connectcampus.colleges.dto;

public record CollegeSummaryDto(
        Long id,
        String name,
        String city,
        String state,
        Integer fees,
        String website,
        String phone
) {}
