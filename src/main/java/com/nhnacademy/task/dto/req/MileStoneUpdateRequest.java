package com.nhnacademy.task.dto.req;

import java.time.LocalDateTime;

public record MileStoneUpdateRequest(
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate
) {}
