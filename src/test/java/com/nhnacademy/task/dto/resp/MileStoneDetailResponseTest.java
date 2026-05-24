package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.MilestoneEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MileStoneDetailResponseTest {

    @Test
    void testFromMilestoneEntity() {
        MilestoneEntity milestoneEntity = Mockito.mock(MilestoneEntity.class);
        LocalDateTime now = LocalDateTime.now();

        when(milestoneEntity.getId()).thenReturn(1L);
        when(milestoneEntity.getName()).thenReturn("Test Milestone");
        when(milestoneEntity.getStartDate()).thenReturn(now);
        when(milestoneEntity.getEndDate()).thenReturn(now.plusDays(7));
        when(milestoneEntity.getCreatedAt()).thenReturn(now.minusDays(1));

        MileStoneDetailResponse response = MileStoneDetailResponse.from(milestoneEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Test Milestone");
        assertThat(response.startDate()).isEqualTo(now);
        assertThat(response.endDate()).isEqualTo(now.plusDays(7));
        assertThat(response.createdAt()).isEqualTo(now.minusDays(1));
    }
}
