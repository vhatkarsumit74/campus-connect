package com.aura.connectcampus.colleges;

import com.aura.connectcampus.common.Exam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EligibilityService {

    private final CutoffRepository cutoffRepository;

    public List<College> searchEligible(Exam exam, short year, BigDecimal studentPercentile) {
        return cutoffRepository.findEligible(exam, year, studentPercentile);
    }
}
