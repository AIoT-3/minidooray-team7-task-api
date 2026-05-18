package com.nhnacademy.task.project.dto.resp;

import com.nhnacademy.task.milestone.dto.resp.MileStoneSimpleResponse;
import com.nhnacademy.task.task.dto.resp.TaskSimpleResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailReponse(
        long id,
        String name,
        String state,
        LocalDateTime createdAt,
        List<MileStoneSimpleResponse> mileStoneList,
        List<TaskSimpleResponse> taskList,
        List<ProjectMemberResponse> memberList
) {
}
