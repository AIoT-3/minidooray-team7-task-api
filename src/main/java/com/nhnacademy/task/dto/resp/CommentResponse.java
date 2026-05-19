package com.nhnacademy.task.dto.resp;

import java.time.LocalDateTime;

public record CommentResponse(
        long id,
        long projectMemberId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
