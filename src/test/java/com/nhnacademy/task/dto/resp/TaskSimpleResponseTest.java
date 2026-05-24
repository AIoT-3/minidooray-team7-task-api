package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.MilestoneEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.entity.TaskTagEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TaskSimpleResponseTest {

    @Test
    void testFromTaskEntity() {
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        ProjectMemberEntity projectMemberEntity = Mockito.mock(ProjectMemberEntity.class);
        MilestoneEntity milestoneEntity = Mockito.mock(MilestoneEntity.class);
        TaskTagEntity taskTagEntity = Mockito.mock(TaskTagEntity.class);

        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getProjectMember()).thenReturn(projectMemberEntity);
        when(projectMemberEntity.getId()).thenReturn(10L);
        when(taskEntity.getName()).thenReturn("Test Task");
        when(taskEntity.getMilestone()).thenReturn(milestoneEntity);
        when(taskEntity.getTaskTagList()).thenReturn(List.of(taskTagEntity));

        when(milestoneEntity.getId()).thenReturn(100L);
        when(milestoneEntity.getName()).thenReturn("Test Milestone");

        TagResponse tagResponse = new TagResponse(200L, "Test Tag");

        when(taskTagEntity.getTag()).thenReturn(Mockito.mock(com.nhnacademy.task.entity.TagEntity.class));
        when(taskTagEntity.getTag().getId()).thenReturn(200L);
        when(taskTagEntity.getTag().getName()).thenReturn("Test Tag");


        TaskSimpleResponse response = TaskSimpleResponse.from(taskEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.projectMemberId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Test Task");
        assertThat(response.mileStone()).isNotNull();
        assertThat(response.mileStone().id()).isEqualTo(100L);
        assertThat(response.tagList()).hasSize(1);
        assertThat(response.tagList().get(0).id()).isEqualTo(200L);
    }

    @Test
    void testFromTaskEntityWithNulls() {
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        ProjectMemberEntity projectMemberEntity = Mockito.mock(ProjectMemberEntity.class);

        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getProjectMember()).thenReturn(projectMemberEntity);
        when(projectMemberEntity.getId()).thenReturn(10L);
        when(taskEntity.getName()).thenReturn("Test Task");
        when(taskEntity.getMilestone()).thenReturn(null);
        when(taskEntity.getTaskTagList()).thenReturn(Collections.emptyList());

        TaskSimpleResponse response = TaskSimpleResponse.from(taskEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.projectMemberId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Test Task");
        assertThat(response.mileStone()).isNull();
        assertThat(response.tagList()).isEmpty();
    }
}
