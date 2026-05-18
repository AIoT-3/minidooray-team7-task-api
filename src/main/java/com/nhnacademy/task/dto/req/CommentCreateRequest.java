package com.nhnacademy.task.dto.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CommentCreateRequest(
        @Length(max = 45)
        @NotBlank
        String content
) {}
