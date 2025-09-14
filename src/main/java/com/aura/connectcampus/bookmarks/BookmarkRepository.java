package com.aura.connectcampus.bookmarks;

import com.aura.connectcampus.colleges.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    boolean existsByIdUserIdAndIdCollegeId(Long userId, Long collegeId);
    void deleteByIdUserIdAndIdCollegeId(Long userId, Long collegeId);
    List<Bookmark> findAllByIdUserId(Long userId);
    List<Bookmark> findAllByIdUserIdOrderByCreatedAtDesc(Long userId);
    long countByCollege(College college);
}
