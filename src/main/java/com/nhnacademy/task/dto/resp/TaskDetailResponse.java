package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.TaskEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
) {
    public static TaskDetailResponse from(TaskEntity taskEntity) {
        return new TaskDetailResponse(
                taskEntity.getId(),
                taskEntity.getProject().getId(),
                taskEntity.getProjectMember().getId(),
                taskEntity.getMilestone() != null ?
                        MileStoneDetailResponse.from(taskEntity.getMilestone()) : null,
                taskEntity.getName(),
                taskEntity.getContent(),
                taskEntity.getCreatedAt(),

                taskEntity.getTaskTagList() != null ?
                        taskEntity.getTaskTagList().stream().map(
                                TagResponse::from
                        ).toList() : new ArrayList<>(),

                taskEntity.getCommentList() != null ?
                        taskEntity.getCommentList().stream().map(
                                CommentResponse::from
                        ).toList() : new ArrayList<>()
        );
    }
}
