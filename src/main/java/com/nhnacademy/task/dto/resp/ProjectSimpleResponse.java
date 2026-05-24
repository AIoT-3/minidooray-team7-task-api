package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectState;

import java.time.LocalDateTime;

public record ProjectSimpleResponse(
        Long id,
        String name,
        String state,
        LocalDateTime createdAt) {

    public ProjectSimpleResponse(Long id, String name, ProjectState state, LocalDateTime createdAt) {
        this(id, name, state.name(), createdAt);
    }

    public static ProjectSimpleResponse from(ProjectEntity projectEntity) {
        return new ProjectSimpleResponse(
                projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getState().name(),
                projectEntity.getCreatedAt()
        );
    }
}
