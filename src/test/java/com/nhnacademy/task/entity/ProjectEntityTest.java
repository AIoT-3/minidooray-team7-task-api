package com.nhnacademy.task.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectEntityTest {

    @Test
    void testProjectEntityCreation() {
        String name = "Test Project";
        ProjectState state = ProjectState.ACTIVE;
        ProjectEntity project = new ProjectEntity(name, state);

        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getState()).isEqualTo(state);
        assertThat(project.getCreatedAt()).isNotNull();
    }

    @Test
    void testProjectEntityCreationWithDateTime() {
        String name = "Test Project";
        ProjectState state = ProjectState.ACTIVE;
        LocalDateTime now = LocalDateTime.now();
        ProjectEntity project = new ProjectEntity(name, state, now);

        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getState()).isEqualTo(state);
        assertThat(project.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testUpdateNameAndState() {
        ProjectEntity project = new ProjectEntity("Old Name", ProjectState.ACTIVE);
        String newName = "New Name";
        ProjectState newState = ProjectState.DORMANT;

        project.updateNameAndState(newName, newState);

        assertThat(project.getName()).isEqualTo(newName);
        assertThat(project.getState()).isEqualTo(newState);
    }
}
