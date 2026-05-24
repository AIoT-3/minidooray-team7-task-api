package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TaskDetailResponseTest {

    @Test
    void testFromTaskEntity() {
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        ProjectEntity projectEntity = Mockito.mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = Mockito.mock(ProjectMemberEntity.class);
        MilestoneEntity milestoneEntity = Mockito.mock(MilestoneEntity.class);
        TaskTagEntity taskTagEntity = Mockito.mock(TaskTagEntity.class);
        CommentEntity commentEntity = Mockito.mock(CommentEntity.class);
        
        LocalDateTime now = LocalDateTime.now();

        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getProject()).thenReturn(projectEntity);
        when(projectEntity.getId()).thenReturn(5L);
        when(taskEntity.getProjectMember()).thenReturn(projectMemberEntity);
        when(projectMemberEntity.getId()).thenReturn(10L);
        when(taskEntity.getMilestone()).thenReturn(milestoneEntity);
        when(taskEntity.getName()).thenReturn("Test Task");
        when(taskEntity.getContent()).thenReturn("Test Content");
        when(taskEntity.getCreatedAt()).thenReturn(now);
        when(taskEntity.getTaskTagList()).thenReturn(List.of(taskTagEntity));
        when(taskEntity.getCommentList()).thenReturn(List.of(commentEntity));


        when(milestoneEntity.getId()).thenReturn(100L);
        when(commentEntity.getId()).thenReturn(300L);
        when(commentEntity.getProjectMember()).thenReturn(projectMemberEntity);
        when(commentEntity.getContent()).thenReturn("Test Comment");
        when(commentEntity.getCreatedAt()).thenReturn(now);
        when(commentEntity.getUpdatedAt()).thenReturn(now);


        TagEntity tagEntity = Mockito.mock(TagEntity.class);
        when(taskTagEntity.getTag()).thenReturn(tagEntity);
        when(tagEntity.getId()).thenReturn(200L);
        when(tagEntity.getName()).thenReturn("Test Tag");


        TaskDetailResponse response = TaskDetailResponse.from(taskEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.projectId()).isEqualTo(5L);
        assertThat(response.projectMemberId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Test Task");
        assertThat(response.content()).isEqualTo("Test Content");
        assertThat(response.createdAt()).isEqualTo(now);
        assertThat(response.mileStone()).isNotNull();
        assertThat(response.mileStone().id()).isEqualTo(100L);
        assertThat(response.tags()).hasSize(1);
        assertThat(response.tags().get(0).id()).isEqualTo(200L);
        assertThat(response.comments()).hasSize(1);
        assertThat(response.comments().get(0).id()).isEqualTo(300L);
    }

    @Test
    void testFromTaskEntityWithNulls() {
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        ProjectEntity projectEntity = Mockito.mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = Mockito.mock(ProjectMemberEntity.class);

        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getProject()).thenReturn(projectEntity);
        when(projectEntity.getId()).thenReturn(5L);
        when(taskEntity.getProjectMember()).thenReturn(projectMemberEntity);
        when(projectMemberEntity.getId()).thenReturn(10L);
        when(taskEntity.getName()).thenReturn("Test Task");
        when(taskEntity.getMilestone()).thenReturn(null);
        when(taskEntity.getTaskTagList()).thenReturn(Collections.emptyList());
        when(taskEntity.getCommentList()).thenReturn(Collections.emptyList());


        TaskDetailResponse response = TaskDetailResponse.from(taskEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.mileStone()).isNull();
        assertThat(response.tags()).isEmpty();
        assertThat(response.comments()).isEmpty();
    }
}
