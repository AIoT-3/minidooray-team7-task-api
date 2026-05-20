package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.MilestoneEntity;

import java.time.LocalDateTime;

public record MileStoneDetailResponse(
        long id,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime createdAt
) {
    public static MileStoneDetailResponse from(MilestoneEntity milestoneEntity) {
        return new MileStoneDetailResponse(
                milestoneEntity.getId(),
                milestoneEntity.getName(),
                milestoneEntity.getStartDate(),
                milestoneEntity.getEndDate(),
                milestoneEntity.getCreatedAt()
        );
    }
}
