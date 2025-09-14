package com.aura.connectcampus.colleges;

import com.aura.connectcampus.colleges.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/colleges")
@RequiredArgsConstructor
public class CollegeAdminController {

    private final CollegeService collegeService;

    // ----- Colleges -----
    @PostMapping
    public ResponseEntity<CollegeSummaryDto> createCollege(@RequestBody @Valid CollegeCreateRequest req) {
        return ResponseEntity.ok(collegeService.createCollege(req));
    }

    @PutMapping("/{collegeId}")
    public ResponseEntity<CollegeSummaryDto> updateCollege(
            @PathVariable Long collegeId,
            @RequestBody @Valid CollegeUpdateRequest req
    ) {
        return ResponseEntity.ok(collegeService.updateCollege(collegeId, req));
    }

    @DeleteMapping("/{collegeId}")
    public ResponseEntity<Void> deleteCollege(@PathVariable Long collegeId) {
        collegeService.deleteCollege(collegeId);
        return ResponseEntity.noContent().build();
    }

    // ----- Programs -----
    @PostMapping("/{collegeId}/programs")
    public ResponseEntity<ProgramDto> addProgram(
            @PathVariable Long collegeId,
            @RequestBody @Valid ProgramRequest req
    ) {
        return ResponseEntity.ok(collegeService.addProgram(collegeId, req));
    }

    @PutMapping("/programs/{programId}")
    public ResponseEntity<ProgramDto> updateProgram(
            @PathVariable Long programId,
            @RequestBody @Valid ProgramRequest req
    ) {
        return ResponseEntity.ok(collegeService.updateProgram(programId, req));
    }

    @DeleteMapping("/programs/{programId}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long programId) {
        collegeService.deleteProgram(programId);
        return ResponseEntity.noContent().build();
    }

    // ----- Cutoffs -----
    @PostMapping("/programs/{programId}/cutoffs")
    public ResponseEntity<CutoffDto> addCutoff(
            @PathVariable Long programId,
            @RequestBody @Valid CutoffRequest req
    ) {
        return ResponseEntity.ok(collegeService.addCutoff(programId, req));
    }

    @PutMapping("/cutoffs/{cutoffId}")
    public ResponseEntity<CutoffDto> updateCutoff(
            @PathVariable Long cutoffId,
            @RequestBody @Valid CutoffRequest req
    ) {
        return ResponseEntity.ok(collegeService.updateCutoff(cutoffId, req));
    }

    @DeleteMapping("/cutoffs/{cutoffId}")
    public ResponseEntity<Void> deleteCutoff(@PathVariable Long cutoffId) {
        collegeService.deleteCutoff(cutoffId);
        return ResponseEntity.noContent().build();
    }
}
