package com.aura.connectcampus.colleges;

import com.aura.connectcampus.colleges.dto.*;

import java.util.List;

public final class CollegeMapper {
    private CollegeMapper() {}

    public static CollegeSummaryDto toSummary(College c) {
        return new CollegeSummaryDto(
                c.getId(), c.getName(), c.getCity(), c.getState(), c.getFees(), c.getWebsite(), c.getPhone()
        );
    }

    public static CollegeDetailsDto toDetails(College c) {
        List<ProgramDto> programs = c.getPrograms().stream()
                .map(CollegeMapper::toProgram)
                .toList();
        return new CollegeDetailsDto(
                c.getId(), c.getName(), c.getCity(), c.getState(), c.getFees(), c.getWebsite(), c.getPhone(), programs
        );
    }

    public static ProgramDto toProgram(Program p) {
        List<CutoffDto> cutoffs = p.getCutoffs().stream()
                .map(CollegeMapper::toCutoff)
                .toList();
        return new ProgramDto(p.getId(), p.getName(), p.getDegree(), p.getDurationYears(), cutoffs);
    }

    public static CutoffDto toCutoff(Cutoff c) {
        return new CutoffDto(c.getId(), c.getExam(), c.getYear(), c.getPercentileCutoff());
    }
}
