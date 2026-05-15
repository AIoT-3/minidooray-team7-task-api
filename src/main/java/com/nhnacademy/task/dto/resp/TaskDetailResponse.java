package com.nhnacademy.task.dto.resp;

import java.time.LocalDateTime;
import java.util.List;

public record TaskDetailResponse(
        long id,
        long projectId,
        long projectMemberId,
        MileStoneDetailResponse mileStone,
        String name,
        String content,
        LocalDateTime createdAt,
        List<TagResponse> tags,
        List<CommentResponse> comments
) {}
