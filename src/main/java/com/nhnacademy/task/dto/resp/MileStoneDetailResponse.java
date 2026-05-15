package com.nhnacademy.task.dto.resp;

import java.time.LocalDateTime;

public record MileStoneDetailResponse(
        long id,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime createdAt
) {}
