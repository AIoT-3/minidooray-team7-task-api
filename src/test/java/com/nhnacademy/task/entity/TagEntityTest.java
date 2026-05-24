package com.nhnacademy.task.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TagEntityTest {

    @Mock
    private ProjectEntity mockProject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockProject.getTagList()).thenReturn(new ArrayList<>());
    }

    @Test
    void testTagEntityCreation() {
        String name = "Test Tag";
        TagEntity tag = new TagEntity(mockProject, name);

        assertThat(tag.getName()).isEqualTo(name);
        assertThat(tag.getProject()).isEqualTo(mockProject);
        assertThat(tag.getCreatedAt()).isNotNull();
    }

    @Test
    void testTagEntityCreationWithDateTime() {
        String name = "Test Tag";
        LocalDateTime now = LocalDateTime.now();
        TagEntity tag = new TagEntity(mockProject, name, now);

        assertThat(tag.getName()).isEqualTo(name);
        assertThat(tag.getProject()).isEqualTo(mockProject);
        assertThat(tag.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testSetProject() {
        TagEntity tag = new TagEntity(null, "Tag");

        tag.setProject(mockProject);

        assertThat(tag.getProject()).isEqualTo(mockProject);
        assertThat(mockProject.getTagList()).contains(tag);

        tag.setProject(null);
        assertThat(tag.getProject()).isNull();
    }

    @Test
    void testUpdateName() {
        TagEntity tag = new TagEntity(mockProject, "Old Name");
        String newName = "New Name";

        tag.updateName(newName);

        assertThat(tag.getName()).isEqualTo(newName);
    }
}
