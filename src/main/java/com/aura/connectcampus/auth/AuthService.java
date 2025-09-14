package com.aura.connectcampus.auth;

import com.aura.connectcampus.common.Role;
import com.aura.connectcampus.auth.dto.LoginRequest;
import com.aura.connectcampus.auth.dto.RegisterRequest;
import com.aura.connectcampus.auth.dto.AuthResponse;
import com.aura.connectcampus.users.User;
import com.aura.connectcampus.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .firstName(req.firstName())
                .lastName(req.lastName())
                .role(Role.STUDENT)
                .build();
        userRepo.save(user);
        return new AuthResponse(jwt.generateToken(user.getEmail(), user.getRole()));
    }

    public AuthResponse login(LoginRequest req) {
        var user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return new AuthResponse(jwt.generateToken(user.getEmail(), user.getRole()));
    }
}
