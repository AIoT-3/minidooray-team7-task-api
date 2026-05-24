package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ProjectSimpleResponseTest {

    @Test
    void testFromProjectEntity() {
        ProjectEntity projectEntity = Mockito.mock(ProjectEntity.class);
        LocalDateTime now = LocalDateTime.now();

        when(projectEntity.getId()).thenReturn(1L);
        when(projectEntity.getName()).thenReturn("Test Project");
        when(projectEntity.getState()).thenReturn(ProjectState.ACTIVE);
        when(projectEntity.getCreatedAt()).thenReturn(now);

        ProjectSimpleResponse response = ProjectSimpleResponse.from(projectEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Test Project");
        assertThat(response.state()).isEqualTo("ACTIVE");
        assertThat(response.createdAt()).isEqualTo(now);
    }
}
