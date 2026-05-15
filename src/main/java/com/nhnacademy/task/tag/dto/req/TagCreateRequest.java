package com.nhnacademy.task.tag.dto.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TagCreateRequest(
        @NotBlank
        @Length(max = 50)
        String name
) {}
