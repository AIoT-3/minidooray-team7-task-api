package com.nhnacademy.task.project.dto;

import java.time.LocalDateTime;

public record ProjectSimpleResponseDto(Long id, String name, String state, LocalDateTime createdAt) {
}
