package com.aura.connectcampus.blogs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
    List<BlogComment> findByPostIdOrderByCreatedAtDesc(Long postId);
}
