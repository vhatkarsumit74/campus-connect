package com.aura.connectcampus.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank String token,                 // format: <uuid>.<secret>
        @NotBlank @Size(min = 6, max = 64) String newPassword
) {}
