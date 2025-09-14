package com.aura.connectcampus.colleges.dto;

public record CollegeUpdateRequest(
        String name,
        String city,
        String state,
        Integer fees,
        String website,
        String phone
) {}
