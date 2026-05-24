package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.CommentEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CommentResponseTest {

    @Test
    void testFromCommentEntity() {
        CommentEntity commentEntity = Mockito.mock(CommentEntity.class);
        ProjectMemberEntity projectMemberEntity = Mockito.mock(ProjectMemberEntity.class);

        LocalDateTime now = LocalDateTime.now();
        
        when(commentEntity.getId()).thenReturn(1L);
        when(commentEntity.getProjectMember()).thenReturn(projectMemberEntity);
        when(projectMemberEntity.getId()).thenReturn(10L);
        when(commentEntity.getContent()).thenReturn("Test Content");
        when(commentEntity.getCreatedAt()).thenReturn(now);
        when(commentEntity.getUpdatedAt()).thenReturn(now.plusHours(1));

        CommentResponse response = CommentResponse.from(commentEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.projectMemberId()).isEqualTo(10L);
        assertThat(response.content()).isEqualTo("Test Content");
        assertThat(response.createdAt()).isEqualTo(now);
        assertThat(response.updatedAt()).isEqualTo(now.plusHours(1));
    }
}
