package com.nhnacademy.task.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record MileStoneUpdateRequest(
        @NotBlank
        @Length(max = 50)
        String name,
        @NotNull
        LocalDateTime startDate,
        @NotNull
        LocalDateTime endDate
) {}
