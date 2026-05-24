package com.nhnacademy.task.entity;

import com.nhnacademy.task.dto.req.TaskCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TaskEntityTest {

    @Mock
    private ProjectEntity mockProject;

    @Mock
    private ProjectMemberEntity mockProjectMember;

    @Mock
    private MilestoneEntity mockMilestone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockProject.getTaskList()).thenReturn(new ArrayList<>());
        when(mockProjectMember.getTaskList()).thenReturn(new ArrayList<>());
        when(mockMilestone.getTaskList()).thenReturn(new ArrayList<>());
    }

    @Test
    void testConstructorAndGetters() {
        String name = "Test Task";
        String content = "Test Content";

        TaskEntity task = new TaskEntity(name, mockProject, mockProjectMember, content);

        assertThat(task.getName()).isEqualTo(name);
        assertThat(task.getContent()).isEqualTo(content);
        assertThat(task.getProject()).isEqualTo(mockProject);
        assertThat(task.getProjectMember()).isEqualTo(mockProjectMember);
        assertThat(task.getCreatedAt()).isNotNull();
    }

    @Test
    void testSetProject() {
        TaskEntity task = new TaskEntity("Task", null, null, "Content");
        
        task.setProject(mockProject);

        assertThat(task.getProject()).isEqualTo(mockProject);
        assertThat(mockProject.getTaskList()).contains(task);

        task.setProject(null);
        assertThat(task.getProject()).isNull();
    }

    @Test
    void testSetProjectMember() {
        TaskEntity task = new TaskEntity("Task", null, null, "Content");

        task.setProjectMember(mockProjectMember);

        assertThat(task.getProjectMember()).isEqualTo(mockProjectMember);
        assertThat(mockProjectMember.getTaskList()).contains(task);

        task.setProjectMember(null);
        assertThat(task.getProjectMember()).isNull();
    }

    @Test
    void testSetMilestone() {
        TaskEntity task = new TaskEntity("Task", null, null, "Content");

        task.setMilestone(mockMilestone);

        assertThat(task.getMilestone()).isEqualTo(mockMilestone);
        assertThat(mockMilestone.getTaskList()).contains(task);

        task.setMilestone(null);
        assertThat(task.getMilestone()).isNull();
    }

    @Test
    void testUpdateNameAndContent() {
        TaskEntity task = new TaskEntity("Old Name", null, null, "Old Content");
        String newName = "New Name";
        String newContent = "New Content";

        task.updateNameAndContent(newName, newContent);

        assertThat(task.getName()).isEqualTo(newName);
        assertThat(task.getContent()).isEqualTo(newContent);
    }

    @Test
    void testCreateFromRequest() {
        TaskCreateRequest request = new TaskCreateRequest("Request Name", "Request Content");

        TaskEntity task = TaskEntity.create(request, mockProject, mockProjectMember);

        assertThat(task.getName()).isEqualTo("Request Name");
        assertThat(task.getContent()).isEqualTo("Request Content");
        assertThat(task.getProject()).isEqualTo(mockProject);
        assertThat(task.getProjectMember()).isEqualTo(mockProjectMember);
    }
}
