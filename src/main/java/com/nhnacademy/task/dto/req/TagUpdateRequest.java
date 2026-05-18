package com.nhnacademy.task.dto.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TagUpdateRequest(
        @NotBlank
        @Length(max = 50)
        String name
) {}
