package com.aura.connectcampus.security;

import com.aura.connectcampus.common.Role;
import com.aura.connectcampus.users.User;
import com.aura.connectcampus.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUser {
    private final UserRepository userRepository;

    public String email() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? null : (String) auth.getPrincipal();
    }

    public boolean isAuthenticated() {
        return email() != null;
    }

    public boolean isAdmin() {
        return require().getRole() == Role.ADMIN;
    }

    public User require() {
        String email = email();
        if (email == null) throw new IllegalStateException("No authenticated user");
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
