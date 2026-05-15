package com.nhnacademy.task.task.dto.resp;

import java.util.List;

public record TaskSimpleResponse(
        long id,
        long projectMemberId,
        String name,
        MileStoneSimpleResponse mileStone,
        List<TagResponse> tagList
) {}
