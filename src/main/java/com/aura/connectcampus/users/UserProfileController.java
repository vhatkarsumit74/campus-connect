package com.aura.connectcampus.users;

import com.aura.connectcampus.security.CurrentUser;
import com.aura.connectcampus.users.dto.UserProfileDto;
import com.aura.connectcampus.users.dto.UserProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final CurrentUser currentUser;
    private final UserRepository userRepo;

    @GetMapping
    public UserProfileDto me() {
        var u = currentUser.require();
        return new UserProfileDto(
                u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getAge(),
                u.getMarks12th(), u.getCetPercentile(), u.getJeePercentile(), u.getRole(), u.isVerified()
        );
    }

    @PutMapping
    public ResponseEntity<UserProfileDto> update(@RequestBody UserProfileUpdateRequest req) {
        var u = currentUser.require();

        if (req.firstName() != null) u.setFirstName(req.firstName());
        if (req.lastName() != null) u.setLastName(req.lastName());
        if (req.age() != null) u.setAge(req.age());
        if (req.marks12th() != null) u.setMarks12th(req.marks12th());
        if (req.cetPercentile() != null) u.setCetPercentile(req.cetPercentile());
        if (req.jeePercentile() != null) u.setJeePercentile(req.jeePercentile());

        userRepo.save(u);

        return ResponseEntity.ok(new UserProfileDto(
                u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getAge(),
                u.getMarks12th(), u.getCetPercentile(), u.getJeePercentile(), u.getRole(), u.isVerified()
        ));
    }
}
