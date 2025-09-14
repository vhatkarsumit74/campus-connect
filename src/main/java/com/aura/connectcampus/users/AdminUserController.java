package com.aura.connectcampus.users;

import com.aura.connectcampus.common.exceptions.ResourceNotFoundException;
import com.aura.connectcampus.users.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService service;
    private final UserRepository userRepo;

    @GetMapping
    public Page<AdminUserSummaryDto> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public AdminUserDetailsDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<AdminUserSummaryDto> updateRole(@PathVariable Long id, @RequestBody @Valid AdminUpdateRoleRequest req) {
        var u = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        u.setRole(req.role());
        userRepo.save(u);
        return ResponseEntity.ok(new AdminUserSummaryDto(u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getRole(), u.isVerified()));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<AdminUserSummaryDto> verify(@PathVariable Long id, @RequestBody @Valid AdminVerifyUserRequest req) {
        var u = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        u.setVerified(req.verified());
        userRepo.save(u);
        return ResponseEntity.ok(new AdminUserSummaryDto(u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getRole(), u.isVerified()));
    }
}
