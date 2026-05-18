package com.nhnacademy.task.comment.dto.resp;

import java.time.LocalDateTime;

public record CommentResponse(
        long id,
        long projectMemberId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
