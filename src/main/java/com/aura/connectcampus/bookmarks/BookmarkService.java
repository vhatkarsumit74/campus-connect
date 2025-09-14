package com.aura.connectcampus.bookmarks;

import com.aura.connectcampus.colleges.College;
import com.aura.connectcampus.colleges.CollegeMapper;
import com.aura.connectcampus.colleges.CollegeRepository;
import com.aura.connectcampus.colleges.dto.CollegeSummaryDto;
import com.aura.connectcampus.common.exceptions.ResourceNotFoundException;
import com.aura.connectcampus.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepo;
    private final CollegeRepository collegeRepo;
    private final CurrentUser currentUser;

    @Transactional
    public void add(Long collegeId) {
        var user = currentUser.require();
        if (!collegeRepo.existsById(collegeId)) throw new ResourceNotFoundException("College not found: " + collegeId);
        if (bookmarkRepo.existsByIdUserIdAndIdCollegeId(user.getId(), collegeId)) return; // idempotent

        College college = collegeRepo.getReferenceById(collegeId);
        Bookmark b = Bookmark.builder()
                .id(new BookmarkId(user.getId(), collegeId))
                .user(user)
                .college(college)
                .build();
        bookmarkRepo.save(b);
    }

    @Transactional
    public void remove(Long collegeId) {
        var user = currentUser.require();
        bookmarkRepo.deleteByIdUserIdAndIdCollegeId(user.getId(), collegeId);
    }

    @Transactional(readOnly = true)
    public List<CollegeSummaryDto> list() {
        var user = currentUser.require();
        return bookmarkRepo.findAllByIdUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(b -> CollegeMapper.toSummary(b.getCollege()))
                .toList();
    }
}
