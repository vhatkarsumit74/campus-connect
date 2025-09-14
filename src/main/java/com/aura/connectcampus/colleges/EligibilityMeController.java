package com.aura.connectcampus.colleges;

import com.aura.connectcampus.colleges.dto.CollegeSummaryDto;
import com.aura.connectcampus.common.Exam;
import com.aura.connectcampus.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityMeController {

    private final EligibilityService eligibilityService;
    private final CurrentUser currentUser;

    /**
     * Use the authenticated student's saved CET/JEE percentile to find eligible colleges.
     * Example: GET /api/eligibility/mine?exam=CET&year=2025
     */
    @GetMapping("/mine")
    public List<CollegeSummaryDto> mine(@RequestParam Exam exam, @RequestParam short year) {
        var u = currentUser.require();

        BigDecimal percentile = switch (exam) {
            case CET -> u.getCetPercentile();
            case JEE -> u.getJeePercentile();
        };

        if (percentile == null) {
            throw new IllegalArgumentException("Your profile doesn't have " + exam + " percentile. Update it in /api/me.");
        }

        return eligibilityService.searchEligible(exam, year, percentile)
                .stream().map(CollegeMapper::toSummary).toList();
    }
}
