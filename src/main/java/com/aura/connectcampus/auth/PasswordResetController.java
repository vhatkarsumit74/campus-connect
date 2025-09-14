package com.aura.connectcampus.auth;

import com.aura.connectcampus.auth.dto.ForgotPasswordRequest;
import com.aura.connectcampus.auth.dto.ResetPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService service;

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgot(@RequestBody @Valid ForgotPasswordRequest req) {
        service.requestReset(req);
        // Always 200 to avoid leaking which emails exist
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset(@RequestBody @Valid ResetPasswordRequest req) {
        service.reset(req);
        return ResponseEntity.ok().build();
    }
}
