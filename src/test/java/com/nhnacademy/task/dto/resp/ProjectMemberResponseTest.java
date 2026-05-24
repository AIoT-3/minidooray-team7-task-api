package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.ProjectMemberRole;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ProjectMemberResponseTest {

    @Test
    void testFromProjectMemberEntity() {
        ProjectMemberEntity projectMemberEntity = Mockito.mock(ProjectMemberEntity.class);
        
        when(projectMemberEntity.getId()).thenReturn(1L);
        when(projectMemberEntity.getUserId()).thenReturn(100L);
        when(projectMemberEntity.getRole()).thenReturn(ProjectMemberRole.ADMIN);
        when(projectMemberEntity.getIsDeleted()).thenReturn(false);

        ProjectMemberResponse response = ProjectMemberResponse.from(projectMemberEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(100L);
        assertThat(response.role()).isEqualTo("ADMIN");
        assertThat(response.isDeleted()).isFalse();
    }
}
