package com.aura.connectcampus.colleges;

import com.aura.connectcampus.colleges.dto.CollegeDetailsDto;
import com.aura.connectcampus.colleges.dto.CollegeSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colleges")
@RequiredArgsConstructor
public class CollegePublicController {

    private final CollegeService collegeService;

    @GetMapping
    public Page<CollegeSummaryDto> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Integer minFees,
            @RequestParam(required = false) Integer maxFees,
            Pageable pageable
    ) {
        return collegeService.listPublic(q, city, state, minFees, maxFees, pageable);
    }

    @GetMapping("/{id}")
    public CollegeDetailsDto details(@PathVariable Long id) {
        return collegeService.getDetails(id);
    }
}
