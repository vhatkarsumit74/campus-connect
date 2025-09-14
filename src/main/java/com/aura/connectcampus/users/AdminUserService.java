package com.aura.connectcampus.users;

import com.aura.connectcampus.common.exceptions.ResourceNotFoundException;
import com.aura.connectcampus.users.dto.AdminUserDetailsDto;
import com.aura.connectcampus.users.dto.AdminUserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepo;

    public Page<AdminUserSummaryDto> list(Pageable pageable) {
        return userRepo.findAll(pageable)
                .map(u -> new AdminUserSummaryDto(u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getRole(), u.isVerified()));
    }

    public AdminUserDetailsDto get(Long id) {
        var u = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return new AdminUserDetailsDto(
                u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getAge(),
                u.getMarks12th(), u.getCetPercentile(), u.getJeePercentile(), u.getRole(), u.isVerified()
        );
    }

    public void delete(Long id) {
        if (!userRepo.existsById(id)) throw new ResourceNotFoundException("User not found: " + id);
        userRepo.deleteById(id);
    }
}
