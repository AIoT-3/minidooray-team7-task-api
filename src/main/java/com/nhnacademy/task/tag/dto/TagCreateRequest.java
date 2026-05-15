package com.nhnacademy.task.tag.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * TagCreateRequest
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public record TagCreateRequest(
        @NotBlank
        @Length(max = 50)
        String name
) {
}
