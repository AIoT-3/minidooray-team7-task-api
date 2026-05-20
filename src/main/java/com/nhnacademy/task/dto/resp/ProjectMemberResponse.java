package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;

public record ProjectMemberResponse(
        long id,
        long userId,
        String role,
        boolean isDeleted
) {
    public static ProjectMemberResponse from(ProjectMemberEntity projectMemberEntity) {
        return new ProjectMemberResponse(
                projectMemberEntity.getId(),
                projectMemberEntity.getUserId(),
                projectMemberEntity.getRole().name(),
                projectMemberEntity.getIsDeleted()
        );
    }
}
