package com.nhnacademy.task.dto.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TaskCreateRequest(
        @NotBlank
        @Length(max = 200)
        String name,

        @NotBlank
        @Length(max = 2000)
        String content
) {}
