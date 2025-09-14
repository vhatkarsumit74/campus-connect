package com.aura.connectcampus.auth;

import com.aura.connectcampus.auth.dto.ForgotPasswordRequest;
import com.aura.connectcampus.auth.dto.ResetPasswordRequest;
import com.aura.connectcampus.common.MailService;
import com.aura.connectcampus.users.User;
import com.aura.connectcampus.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder encoder;
    private final MailService mail;

    @Value("${app.reset.base-url:http://localhost:5173/reset-password}")
    private String resetBaseUrl;

    @Value("${app.reset.expiry-minutes:30}")
    private long expiryMinutes;

    public void requestReset(ForgotPasswordRequest req) {
        User user = userRepo.findByEmail(req.email()).orElse(null);
        // Always act as if it's fine to avoid email enumeration leaks
        if (user == null) return;

        UUID id = UUID.randomUUID();
        String secret = randomUrlSafe(32);
        String tokenToSend = id + "." + secret;

        PasswordResetToken token = PasswordResetToken.builder()
                .id(id)
                .user(user)
                .secretHash(encoder.encode(secret))
                .expiresAt(Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES))
                .used(false)
                .build();
        tokenRepo.save(token);

        String link = resetBaseUrl + "?token=" + tokenToSend;
        String body = """
                Hello %s,
                
                You (or someone) requested a password reset on ConnectCampus.
                Click the link below to set a new password (valid for %d minutes):
                
                %s
                
                If you didn't request this, you can ignore this email.
                """.formatted(user.getFirstName() == null ? user.getEmail() : user.getFirstName(),
                expiryMinutes, link);

        mail.send(user.getEmail(), "Reset your ConnectCampus password", body);
    }

    public void reset(ResetPasswordRequest req) {
        String[] parts = req.token().split("\\.");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid reset token");
        UUID id = UUID.fromString(parts[0]);
        String secret = parts[1];

        PasswordResetToken token = tokenRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (token.isUsed() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        if (!encoder.matches(secret, token.getSecretHash())) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        User user = token.getUser();
        user.setPasswordHash(encoder.encode(req.newPassword()));
        userRepo.save(user);

        token.setUsed(true);
        tokenRepo.save(token);
    }

    private static String randomUrlSafe(int numBytes) {
        byte[] bytes = new byte[numBytes];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
