package com.nhnacademy.task.dto.resp;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponse(
        long id,
        String name,
        String state,
        LocalDateTime createdAt,
        List<MileStoneSimpleResponse> mileStoneList,
        List<TaskSimpleResponse> taskList,
        List<ProjectMemberResponse> memberList
) {
}
