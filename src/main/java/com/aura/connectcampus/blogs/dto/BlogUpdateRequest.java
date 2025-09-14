package com.aura.connectcampus.blogs.dto;

public record BlogUpdateRequest(String title, String slug, String summary, String content, String authorName) {}

