package com.nhnacademy.task.comment.dto;

import java.time.LocalDateTime;

/**
 * CommentDto
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public record CommentResponse(
        Long id,
        Long projectMemberId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
