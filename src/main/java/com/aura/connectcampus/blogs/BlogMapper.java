package com.aura.connectcampus.blogs;

import com.aura.connectcampus.blogs.dto.*;

import java.util.List;

public final class BlogMapper {
    private BlogMapper() {}

    public static BlogPostSummaryDto toSummary(BlogPost p) {
        return new BlogPostSummaryDto(p.getId(), p.getTitle(), p.getSlug(), p.getSummary(), p.getCreatedAt());
    }

    public static BlogPostDetailsDto toDetails(BlogPost p, List<BlogComment> comments) {
        List<BlogCommentDto> cmts = comments.stream().map(BlogMapper::toComment).toList();
        return new BlogPostDetailsDto(p.getId(), p.getTitle(), p.getSlug(), p.getSummary(), p.getContent(),
                p.getAuthorName(), p.getCreatedAt(), p.getUpdatedAt(), cmts);
    }

    public static BlogCommentDto toComment(BlogComment c) {
        String author = c.getAuthorName();
        if (author == null && c.getUser() != null) {
            author = c.getUser().getFirstName() != null ? c.getUser().getFirstName() : c.getUser().getEmail();
        }
        return new BlogCommentDto(c.getId(), author, c.getBody(), c.getCreatedAt());
    }
}
