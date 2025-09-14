package com.aura.connectcampus.colleges.dto;

import com.aura.connectcampus.common.Exam;

import java.math.BigDecimal;

public record CutoffDto(Long id, Exam exam, short year, BigDecimal percentileCutoff) {}
