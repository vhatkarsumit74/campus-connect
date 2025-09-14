package com.aura.connectcampus.blogs.dto;

public record BlogCreateRequest(String title, String slug, String summary, String content, String authorName) {}

