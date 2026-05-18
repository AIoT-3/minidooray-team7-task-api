package com.nhnacademy.task.task.dto.resp;

import com.nhnacademy.task.comment.dto.resp.CommentResponse;
import com.nhnacademy.task.milestone.dto.resp.MileStoneDetailResponse;

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
