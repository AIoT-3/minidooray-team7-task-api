package com.nhnacademy.task.milestone.dto.req;

import java.time.LocalDateTime;

public record MileStoneCreateRequest(
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate
) {}
