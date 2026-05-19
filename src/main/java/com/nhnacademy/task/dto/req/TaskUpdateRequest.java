package com.nhnacademy.task.dto.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TaskUpdateRequest(
        @NotBlank
        @Length(max = 200)
        String name,
        @NotBlank
        @Length(max = 200)
        String content
) {
}
