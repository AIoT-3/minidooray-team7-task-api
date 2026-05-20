package com.nhnacademy.task.dto.resp;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String userId,
        String email,
        UserStatus status,
        LocalDateTime createdAt
) {}
