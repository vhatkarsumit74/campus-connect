package com.aura.connectcampus.colleges;

import com.aura.connectcampus.colleges.dto.CollegeSummaryDto;
import com.aura.connectcampus.common.Exam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityController {

    private final EligibilityService eligibilityService;

    @GetMapping
    public List<CollegeSummaryDto> search(
            @RequestParam Exam exam,
            @RequestParam short year,
            @RequestParam BigDecimal percentile
    ) {
        return eligibilityService.searchEligible(exam, year, percentile)
                .stream()
                .map(CollegeMapper::toSummary)
                .toList();
    }
}
