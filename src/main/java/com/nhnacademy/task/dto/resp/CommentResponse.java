package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.CommentEntity;
import com.nhnacademy.task.entity.ProjectEntity;

import java.time.LocalDateTime;

public record CommentResponse(
        long id,
        long projectMemberId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(CommentEntity commentEntity) {
        return new CommentResponse(
                commentEntity.getId(),
                commentEntity.getProjectMember().getId(),
                commentEntity.getContent(),
                commentEntity.getCreatedAt(),
                commentEntity.getUpdatedAt()
        );
    }
}
