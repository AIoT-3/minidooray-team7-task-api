package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.validator.BusinessRuleValidator;
import com.nhnacademy.task.dto.req.TaskCreateRequest;
import com.nhnacademy.task.dto.req.TaskMileStoneCreateRequest;
import com.nhnacademy.task.dto.req.TaskUpdateRequest;
import com.nhnacademy.task.dto.resp.TaskDetailResponse;
import com.nhnacademy.task.dto.resp.TaskSimpleResponse;
import com.nhnacademy.task.entity.*;
import com.nhnacademy.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private BusinessRuleValidator businessRuleValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        TaskCreateRequest request = new TaskCreateRequest("Task Name", "Task Content");
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        ProjectMemberEntity projectMemberEntity = new ProjectMemberEntity(projectEntity, requestingUserId, ProjectMemberRole.MEMBER);

        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findProjectMemberOrThrow(projectId, requestingUserId)).thenReturn(projectMemberEntity);

        taskService.createTask(requestingUserId, projectId, request);

        verify(taskRepository, times(1)).save(any(TaskEntity.class));
        assertThat(projectEntity.getTaskList()).hasSize(1);
        assertThat(projectMemberEntity.getTaskList()).hasSize(1);
    }

    @Test
    void testGetTaskSimpleInfoById() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = spy(new ProjectMemberEntity(projectEntity, 100L, ProjectMemberRole.MEMBER));
        when(projectMemberEntity.getId()).thenReturn(5L);
        
        TaskEntity taskEntity = spy(new TaskEntity("Task Name", projectEntity, projectMemberEntity, "Content"));
        when(taskEntity.getId()).thenReturn(taskId);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findTaskOrThrow(taskId)).thenReturn(taskEntity);
        doNothing().when(businessRuleValidator).validateTaskBelongsToProject(projectId, taskId);

        TaskSimpleResponse response = taskService.getTaskSimpleInfoById(requestingUserId, projectId, taskId);

        assertThat(response.id()).isEqualTo(taskId);
        assertThat(response.name()).isEqualTo("Task Name");
    }

    @Test
    void testGetTaskDetailInfoById() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        when(projectEntity.getId()).thenReturn(projectId);

        ProjectMemberEntity projectMemberEntity = spy(new ProjectMemberEntity(projectEntity, 100L, ProjectMemberRole.MEMBER));
        when(projectMemberEntity.getId()).thenReturn(5L);

        TaskEntity taskEntity = spy(new TaskEntity("Task Name", projectEntity, projectMemberEntity, "Content"));
        when(taskEntity.getId()).thenReturn(taskId);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findTaskOrThrow(taskId)).thenReturn(taskEntity);
        doNothing().when(businessRuleValidator).validateTaskBelongsToProject(projectId, taskId);

        TaskDetailResponse response = taskService.getTaskDetailInfoById(requestingUserId, projectId, taskId);

        assertThat(response.id()).isEqualTo(taskId);
        assertThat(response.name()).isEqualTo("Task Name");
        assertThat(response.content()).isEqualTo("Content");
    }

    @Test
    void testGetTasksByProjectID() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = spy(new ProjectMemberEntity(projectEntity, 100L, ProjectMemberRole.MEMBER));
        when(projectMemberEntity.getId()).thenReturn(5L);

        TaskEntity task1 = spy(new TaskEntity("Task 1", projectEntity, projectMemberEntity, "Content 1"));
        when(task1.getId()).thenReturn(10L);
        
        TaskEntity task2 = spy(new TaskEntity("Task 2", projectEntity, projectMemberEntity, "Content 2"));
        when(task2.getId()).thenReturn(20L);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(taskRepository.findAllByProject_Id(projectId)).thenReturn(List.of(task1, task2));

        List<TaskSimpleResponse> responses = taskService.getTasksByProjectID(requestingUserId, projectId);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(10L);
        assertThat(responses.get(1).id()).isEqualTo(20L);
    }

    @Test
    void testUpdateTask() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest("Updated Name", "Updated Content");
        TaskEntity taskEntity = new TaskEntity("Old Name", null, null, "Old Content");

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findTaskOrThrow(taskId)).thenReturn(taskEntity);
        doNothing().when(businessRuleValidator).validateTaskBelongsToProject(projectId, taskId);

        taskService.updateTask(requestingUserId, projectId, taskId, request);

        assertThat(taskEntity.getName()).isEqualTo("Updated Name");
        assertThat(taskEntity.getContent()).isEqualTo("Updated Content");
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    void testAddMilestoneToTask() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        Long milestoneId = 1L;
        TaskMileStoneCreateRequest request = new TaskMileStoneCreateRequest(milestoneId);
        TaskEntity taskEntity = new TaskEntity("Task", null, null, "Content");
        MilestoneEntity milestoneEntity = new MilestoneEntity("Milestone", null, null, null);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findTaskOrThrow(taskId)).thenReturn(taskEntity);
        doNothing().when(businessRuleValidator).validateTaskBelongsToProject(projectId, taskId);
        when(businessRuleValidator.findMilestoneOrThrow(milestoneId)).thenReturn(milestoneEntity);
        doNothing().when(businessRuleValidator).validateMilestoneBelongsToProject(eq(projectId), any());

        taskService.addMilestoneToTask(requestingUserId, projectId, taskId, request);

        assertThat(taskEntity.getMilestone()).isEqualTo(milestoneEntity);
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    void testRemoveMilestoneOnTask() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        Long milestoneId = 1L;
        MilestoneEntity milestoneEntity = new MilestoneEntity("Milestone", null, null, null);
        TaskEntity taskEntity = new TaskEntity("Task", null, null, "Content");
        taskEntity.setMilestone(milestoneEntity);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findTaskOrThrow(taskId)).thenReturn(taskEntity);
        doNothing().when(businessRuleValidator).validateTaskBelongsToProject(projectId, taskId);
        when(businessRuleValidator.findMilestoneOrThrow(milestoneId)).thenReturn(milestoneEntity);
        doNothing().when(businessRuleValidator).validateMilestoneBelongsToProject(eq(projectId), any());
        doNothing().when(businessRuleValidator).validateMilestoneBelongsToTask(taskId, milestoneId);

        taskService.removeMilestoneOnTask(requestingUserId, projectId, taskId, milestoneId);

        assertThat(taskEntity.getMilestone()).isNull();
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    void testDeleteTask() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        ProjectMemberEntity projectMemberEntity = new ProjectMemberEntity(projectEntity, 1L, ProjectMemberRole.MEMBER);
        MilestoneEntity milestoneEntity = new MilestoneEntity("Milestone", projectEntity, null, null);
        TaskEntity taskEntity = new TaskEntity("Task", projectEntity, projectMemberEntity, "Content");
        taskEntity.setMilestone(milestoneEntity);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findTaskOrThrow(taskId)).thenReturn(taskEntity);
        doNothing().when(businessRuleValidator).validateTaskBelongsToProject(projectId, taskId);

        taskService.deleteTask(requestingUserId, projectId, taskId);

        assertThat(taskEntity.getProject()).isNull();
        assertThat(taskEntity.getProjectMember()).isNull();
        assertThat(taskEntity.getMilestone()).isNull();
        verify(taskRepository, times(1)).delete(taskEntity);
    }
}
