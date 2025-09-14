package com.aura.connectcampus.bookmarks;

import com.aura.connectcampus.colleges.dto.CollegeSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/bookmarks") @RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService service;

    @GetMapping
    public List<CollegeSummaryDto> myBookmarks() {
        return service.list();
    }

    @PostMapping("/{collegeId}")
    public ResponseEntity<Void> add(@PathVariable Long collegeId) {
        service.add(collegeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{collegeId}")
    public ResponseEntity<Void> remove(@PathVariable Long collegeId) {
        service.remove(collegeId);
        return ResponseEntity.noContent().build();
    }
}
