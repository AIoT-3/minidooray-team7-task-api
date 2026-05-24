package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.MilestoneEntity;

import java.time.LocalDateTime;

public record MileStoneSimpleResponse(
        long id,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static MileStoneSimpleResponse from(MilestoneEntity milestoneEntity) {
        return new MileStoneSimpleResponse(
                milestoneEntity.getId(),
                milestoneEntity.getName(),
                milestoneEntity.getStartDate(),
                milestoneEntity.getEndDate()
        );
    }
}
