package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.validator.BusinessRuleValidator;
import com.nhnacademy.task.dto.req.MileStoneCreateRequest;
import com.nhnacademy.task.dto.req.MileStoneUpdateRequest;
import com.nhnacademy.task.dto.resp.MileStoneDetailResponse;
import com.nhnacademy.task.dto.resp.MileStoneSimpleResponse;
import com.nhnacademy.task.entity.MilestoneEntity;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectState;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.repository.MilestoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MilestoneServiceImplTest {

    @InjectMocks
    private MilestoneServiceImpl milestoneService;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private BusinessRuleValidator businessRuleValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMilestoneToProject() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        MileStoneCreateRequest request = new MileStoneCreateRequest("New Milestone", start, end);
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);

        doNothing().when(businessRuleValidator).validateStartAndEndDates(start, end);
        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);

        milestoneService.createMilestoneToProject(requestingUserId, projectId, request);

        verify(milestoneRepository, times(1)).save(any(MilestoneEntity.class));
    }

    @Test
    void testGetMilestoneDetailInfoById() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long milestoneId = 1L;
        MilestoneEntity milestoneEntity = mock(MilestoneEntity.class);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findMilestoneOrThrow(milestoneId)).thenReturn(milestoneEntity);
        doNothing().when(businessRuleValidator).validateMilestoneBelongsToProject(projectId, milestoneId);
        
        when(milestoneEntity.getId()).thenReturn(milestoneId);
        when(milestoneEntity.getName()).thenReturn("Milestone");

        MileStoneDetailResponse response = milestoneService.getMilestoneDetailInfoById(requestingUserId, projectId, milestoneId);

        assertThat(response.id()).isEqualTo(milestoneId);
        assertThat(response.name()).isEqualTo("Milestone");
    }

    @Test
    void testGetMilestoneSimpleInfosByProjectId() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        MilestoneEntity milestone1 = mock(MilestoneEntity.class);
        MilestoneEntity milestone2 = mock(MilestoneEntity.class);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(milestoneRepository.findAllByProject_Id(projectId)).thenReturn(List.of(milestone1, milestone2));

        List<MileStoneSimpleResponse> responses = milestoneService.getMilestoneSimpleInfosByProjectId(requestingUserId, projectId);

        assertThat(responses).hasSize(2);
    }

    @Test
    void testUpdateMilestone() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long milestoneId = 1L;
        LocalDateTime newStart = LocalDateTime.now();
        LocalDateTime newEnd = newStart.plusDays(5);
        MileStoneUpdateRequest request = new MileStoneUpdateRequest("Updated Name", newStart, newEnd);
        MilestoneEntity milestoneEntity = new MilestoneEntity("Old Name", null, null, null);

        doNothing().when(businessRuleValidator).validateStartAndEndDates(newStart, newEnd);
        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findMilestoneOrThrow(milestoneId)).thenReturn(milestoneEntity);
        doNothing().when(businessRuleValidator).validateMilestoneBelongsToProject(projectId, milestoneId);

        milestoneService.updateMilestone(requestingUserId, projectId, milestoneId, request);

        assertThat(milestoneEntity.getName()).isEqualTo("Updated Name");
        assertThat(milestoneEntity.getStartDate()).isEqualTo(newStart);
        assertThat(milestoneEntity.getEndDate()).isEqualTo(newEnd);
        verify(milestoneRepository, times(1)).save(milestoneEntity);
    }

    @Test
    void testDeleteMilestoneOnProject() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long milestoneId = 1L;
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        MilestoneEntity milestoneEntity = new MilestoneEntity("Milestone", projectEntity, null, null);
        TaskEntity task = mock(TaskEntity.class);
        milestoneEntity.getTaskList().add(task);
        projectEntity.getMilestoneList().add(milestoneEntity);

        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findMilestoneOrThrow(milestoneId)).thenReturn(milestoneEntity);
        doNothing().when(businessRuleValidator).validateMilestoneBelongsToProject(projectId, milestoneId);

        milestoneService.deleteMilestoneOnProject(requestingUserId, projectId, milestoneId);

        verify(task, times(1)).setMilestone(null);
        assertThat(projectEntity.getMilestoneList()).isEmpty();
        assertThat(milestoneEntity.getProject()).isNull();
        verify(milestoneRepository, times(1)).save(milestoneEntity);
    }
}
