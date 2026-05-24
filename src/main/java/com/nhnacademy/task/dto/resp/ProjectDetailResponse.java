package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.MilestoneEntity;
import com.nhnacademy.task.entity.ProjectEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record ProjectDetailResponse(
        long id,
        String name,
        String state,
        LocalDateTime createdAt,
        List<TagResponse> tagList,
        List<MileStoneSimpleResponse> mileStoneList,
        List<TaskSimpleResponse> taskList,
        List<ProjectMemberResponse> memberList
) {
    public static ProjectDetailResponse from(ProjectEntity projectEntity) {
        return new ProjectDetailResponse(
                projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getState().name(),
                projectEntity.getCreatedAt(),

                projectEntity.getTagList() != null ?
                        projectEntity.getTagList().stream().map(
                                TagResponse::from
                        ).toList() : new ArrayList<>(),

                projectEntity.getMilestoneList() != null ?
                        projectEntity.getMilestoneList().stream().map(
                                MileStoneSimpleResponse::from
                        ).toList() : new ArrayList<>(),

                projectEntity.getTaskList() != null ?
                        projectEntity.getTaskList().stream().map(
                                TaskSimpleResponse::from
                        ).toList() : new ArrayList<>(),

                projectEntity.getProjectMemberList() != null ?
                        projectEntity.getProjectMemberList().stream().map(
                                ProjectMemberResponse::from
                        ).toList() : new ArrayList<>()
        );
    }
}
