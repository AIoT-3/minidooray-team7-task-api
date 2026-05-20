package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;

public record TaskSimpleResponse(
        long id,
        long projectMemberId,
        String name,
        MileStoneSimpleResponse mileStone,
        List<TagResponse> tagList
) {
    public static TaskSimpleResponse from(TaskEntity taskEntity) {
        return new TaskSimpleResponse(
                taskEntity.getId(),
                taskEntity.getProjectMember().getId(),
                taskEntity.getName(),
                taskEntity.getMilestone() != null ?
                        MileStoneSimpleResponse.from(taskEntity.getMilestone()) : null,
                taskEntity.getTaskTagList() != null ?
                        taskEntity.getTaskTagList().stream().map(
                                TagResponse::from
                        ).toList() : new ArrayList<>()
        );
    }
}
