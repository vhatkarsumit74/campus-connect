package com.aura.connectcampus.auth;

import com.aura.connectcampus.common.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey; // <— use SecretKey
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String issuer;
    private final SecretKey key; //
    private final long expiryMinutes;

    public JwtService(
            @Value("${jwt.issuer:connectcampus}") String issuer,
            @Value("${jwt.secret:change-this-super-secret-please-32-bytes-min}") String secret,
            @Value("${jwt.expiryMinutes:120}") long expiryMinutes
    ) {
        this.issuer = issuer;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // returns SecretKey
        this.expiryMinutes = expiryMinutes;
    }

    public String generateToken(String subjectEmail, Role role) {
        Instant now = Instant.now();
        Instant exp = now.plus(expiryMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .issuer(issuer)
                .subject(subjectEmail)
                .claims(Map.of("role", role.name()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                // Either of these two is fine in 0.13.x:
                .signWith(key) // infers HS algorithm from SecretKey
                // .signWith(key, Jwts.SIG.HS256) // explicit algorithm (recommended)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key) // now matches SecretKey overload
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {
        Object role = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
        return role == null ? null : role.toString();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
