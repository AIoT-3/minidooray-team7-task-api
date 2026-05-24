package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ProjectDetailResponseTest {

    @Test
    void testFromProjectEntity() {
        ProjectEntity projectEntity = Mockito.mock(ProjectEntity.class);
        TagEntity tagEntity = Mockito.mock(TagEntity.class);
        MilestoneEntity milestoneEntity = Mockito.mock(MilestoneEntity.class);
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        ProjectMemberEntity projectMemberEntity = Mockito.mock(ProjectMemberEntity.class);

        LocalDateTime now = LocalDateTime.now();

        when(projectEntity.getId()).thenReturn(1L);
        when(projectEntity.getName()).thenReturn("Test Project");
        when(projectEntity.getState()).thenReturn(ProjectState.ACTIVE);
        when(projectEntity.getCreatedAt()).thenReturn(now);
        when(projectEntity.getTagList()).thenReturn(List.of(tagEntity));
        when(projectEntity.getMilestoneList()).thenReturn(List.of(milestoneEntity));
        when(projectEntity.getTaskList()).thenReturn(List.of(taskEntity));
        when(projectEntity.getProjectMemberList()).thenReturn(List.of(projectMemberEntity));


        when(tagEntity.getId()).thenReturn(10L);
        when(tagEntity.getName()).thenReturn("Test Tag");
        
        when(milestoneEntity.getId()).thenReturn(20L);
        
        when(taskEntity.getId()).thenReturn(30L);
        when(taskEntity.getProjectMember()).thenReturn(projectMemberEntity);
        
        when(projectMemberEntity.getId()).thenReturn(40L);
        when(projectMemberEntity.getRole()).thenReturn(ProjectMemberRole.MEMBER);


        ProjectDetailResponse response = ProjectDetailResponse.from(projectEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Test Project");
        assertThat(response.state()).isEqualTo("ACTIVE");
        assertThat(response.createdAt()).isEqualTo(now);
        assertThat(response.tagList()).hasSize(1);
        assertThat(response.tagList().get(0).id()).isEqualTo(10L);
        assertThat(response.mileStoneList()).hasSize(1);
        assertThat(response.mileStoneList().get(0).id()).isEqualTo(20L);
        assertThat(response.taskList()).hasSize(1);
        assertThat(response.taskList().get(0).id()).isEqualTo(30L);
        assertThat(response.memberList()).hasSize(1);
        assertThat(response.memberList().get(0).id()).isEqualTo(40L);
    }

    @Test
    void testFromProjectEntityWithNulls() {
        ProjectEntity projectEntity = Mockito.mock(ProjectEntity.class);
        LocalDateTime now = LocalDateTime.now();

        when(projectEntity.getId()).thenReturn(1L);
        when(projectEntity.getName()).thenReturn("Test Project");
        when(projectEntity.getState()).thenReturn(ProjectState.ACTIVE);
        when(projectEntity.getCreatedAt()).thenReturn(now);
        when(projectEntity.getTagList()).thenReturn(Collections.emptyList());
        when(projectEntity.getMilestoneList()).thenReturn(null);
        when(projectEntity.getTaskList()).thenReturn(Collections.emptyList());
        when(projectEntity.getProjectMemberList()).thenReturn(Collections.emptyList());

        ProjectDetailResponse response = ProjectDetailResponse.from(projectEntity);

        assertThat(response.tagList()).isEmpty();
        assertThat(response.mileStoneList()).isEmpty();
        assertThat(response.taskList()).isEmpty();
        assertThat(response.memberList()).isEmpty();
    }
}
