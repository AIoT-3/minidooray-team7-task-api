package com.nhnacademy.task.tag.dto;

import java.time.LocalDateTime;

/**
 * TagDto
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public record TagResponseDto(
        Long id,
        String name,
        LocalDateTime createdAt
) {
}
