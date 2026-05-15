package com.nhnacademy.task.comment.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * CommentCreateRequest
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public record CommentCreateRequestDto(
        @Length(max = 45)
        @NotBlank
        String content
) {
}
