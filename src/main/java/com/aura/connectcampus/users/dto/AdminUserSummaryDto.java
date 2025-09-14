package com.aura.connectcampus.users.dto;

import com.aura.connectcampus.common.Role;

public record AdminUserSummaryDto(Long id, String email, String firstName, String lastName, Role role, boolean verified) {}

