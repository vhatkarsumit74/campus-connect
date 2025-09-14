package com.aura.connectcampus.colleges;

import com.aura.connectcampus.colleges.dto.*;
import com.aura.connectcampus.common.Exam;
import com.aura.connectcampus.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepo;
    private final ProgramRepository programRepo;
    private final CutoffRepository cutoffRepo;

    public Page<CollegeSummaryDto> listPublic(String q, String city, String state, Integer minFees, Integer maxFees, Pageable pageable) {
        Specification<College> spec = CollegeSpecs.all(q, city, state, minFees, maxFees);
        return collegeRepo.findAll(spec, pageable).map(CollegeMapper::toSummary);
    }

    public CollegeDetailsDto getDetails(Long id) {
        College college = collegeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College not found: " + id));
        // initialize programs if LAZY (access triggers fetch)
        college.getPrograms().size();
        college.getPrograms().forEach(p -> p.getCutoffs().size());
        return CollegeMapper.toDetails(college);
    }

    // ---------- Admin operations ----------

    @Transactional
    public CollegeSummaryDto createCollege(CollegeCreateRequest req) {
        College c = College.builder()
                .name(req.name())
                .city(req.city())
                .state(req.state())
                .fees(req.fees())
                .website(req.website())
                .phone(req.phone())
                .build();
        collegeRepo.save(c);
        return CollegeMapper.toSummary(c);
    }

    @Transactional
    public CollegeSummaryDto updateCollege(Long id, CollegeUpdateRequest req) {
        College c = collegeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College not found: " + id));

        if (req.name() != null) c.setName(req.name());
        if (req.city() != null) c.setCity(req.city());
        if (req.state() != null) c.setState(req.state());
        if (req.fees() != null) c.setFees(req.fees());
        if (req.website() != null) c.setWebsite(req.website());
        if (req.phone() != null) c.setPhone(req.phone());

        return CollegeMapper.toSummary(c);
    }

    public void deleteCollege(Long id) {
        if (!collegeRepo.existsById(id)) {
            throw new ResourceNotFoundException("College not found: " + id);
        }
        collegeRepo.deleteById(id);
    }

    @Transactional
    public ProgramDto addProgram(Long collegeId, ProgramRequest req) {
        College college = collegeRepo.findById(collegeId)
                .orElseThrow(() -> new ResourceNotFoundException("College not found: " + collegeId));

        Program p = Program.builder()
                .college(college)
                .name(req.name())
                .degree(req.degree())
                .durationYears(req.durationYears())
                .build();

        college.addProgram(p);
        programRepo.save(p);
        return CollegeMapper.toProgram(p);
    }

    @Transactional
    public ProgramDto updateProgram(Long programId, ProgramRequest req) {
        Program p = programRepo.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found: " + programId));

        p.setName(req.name());
        p.setDegree(req.degree());
        p.setDurationYears(req.durationYears());
        return CollegeMapper.toProgram(p);
    }

    public void deleteProgram(Long programId) {
        if (!programRepo.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found: " + programId);
        }
        programRepo.deleteById(programId);
    }

    @Transactional
    public CutoffDto addCutoff(Long programId, CutoffRequest req) {
        Program p = programRepo.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found: " + programId));

        Cutoff c = Cutoff.builder()
                .program(p)
                .exam(req.exam())
                .year(req.year())
                .percentileCutoff(req.percentileCutoff())
                .build();

        p.addCutoff(c);
        cutoffRepo.save(c);
        return CollegeMapper.toCutoff(c);
    }

    @Transactional
    public CutoffDto updateCutoff(Long cutoffId, CutoffRequest req) {
        Cutoff c = cutoffRepo.findById(cutoffId)
                .orElseThrow(() -> new ResourceNotFoundException("Cutoff not found: " + cutoffId));
        c.setExam(req.exam());
        c.setYear(req.year());
        c.setPercentileCutoff(req.percentileCutoff());
        return CollegeMapper.toCutoff(c);
    }

    public void deleteCutoff(Long cutoffId) {
        if (!cutoffRepo.existsById(cutoffId)) {
            throw new ResourceNotFoundException("Cutoff not found: " + cutoffId);
        }
        cutoffRepo.deleteById(cutoffId);
    }
}
