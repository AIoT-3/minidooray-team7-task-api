package com.nhnacademy.task.dto.req;

import jakarta.validation.constraints.NotNull;

public record TaskMileStoneCreateRequest(
        @NotNull
        long mileStoneId
) {}
