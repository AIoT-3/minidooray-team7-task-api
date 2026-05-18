package com.nhnacademy.task.milestone.dto.resp;

import java.time.LocalDateTime;

public record MileStoneDetailResponse(
        long id,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime createdAt
) {}
