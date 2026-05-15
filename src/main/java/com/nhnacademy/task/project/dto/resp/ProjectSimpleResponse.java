package com.nhnacademy.task.project.dto.resp;

import com.nhnacademy.task.project.ProjectState;

import java.time.LocalDateTime;

public record ProjectSimpleResponse(
        Long id,
        String name,
        String state,
        LocalDateTime createdAt) {

    public ProjectSimpleResponse(Long id, String name, ProjectState state, LocalDateTime createdAt) {
        this(id, name, state.name(), createdAt);
    }
}
