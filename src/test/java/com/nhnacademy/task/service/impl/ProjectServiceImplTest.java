package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.validator.BusinessRuleValidator;
import com.nhnacademy.task.dto.req.ProjectCreateRequest;
import com.nhnacademy.task.dto.req.ProjectUpdateRequest;
import com.nhnacademy.task.dto.resp.ProjectDetailResponse;
import com.nhnacademy.task.dto.resp.ProjectSimpleResponse;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.ProjectMemberRole;
import com.nhnacademy.task.entity.ProjectState;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private BusinessRuleValidator businessRuleValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() {
        Long requestingUserId = 1L;
        ProjectCreateRequest request = new ProjectCreateRequest("New Project");
        
        ProjectEntity projectEntity = new ProjectEntity("New Project", ProjectState.ACTIVE);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        projectService.createProject(requestingUserId, request);

        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
        verify(projectMemberRepository, times(1)).save(any(ProjectMemberEntity.class));
    }

    @Test
    void testGetProjectSimpleInfoById() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectEntity projectEntity = spy(new ProjectEntity("Test Project", ProjectState.ACTIVE, LocalDateTime.now()));
        when(projectEntity.getId()).thenReturn(projectId);
        
        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);

        ProjectSimpleResponse response = projectService.getProjectSimpleInfoById(requestingUserId, projectId);

        assertThat(response.id()).isEqualTo(projectId);
        assertThat(response.name()).isEqualTo("Test Project");
        verify(businessRuleValidator, times(1)).findProjectOrThrow(projectId);
        verify(businessRuleValidator, times(1)).validateProjectMembership(requestingUserId, projectId);
    }

    @Test
    void testGetProjectsByUserId() {
        Long requestingUserId = 1L;
        ProjectSimpleResponse simpleResponse = new ProjectSimpleResponse(1L, "Test Project", "ACTIVE", LocalDateTime.now());
        when(projectRepository.findAllByUserId(requestingUserId)).thenReturn(List.of(simpleResponse));

        List<ProjectSimpleResponse> responses = projectService.getProjectsByUserId(requestingUserId);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("Test Project");
        verify(projectRepository, times(1)).findAllByUserId(requestingUserId);
    }
    
    @Test
    void testGetProjectsByUserId_whenEmpty() {
        Long requestingUserId = 1L;
        when(projectRepository.findAllByUserId(requestingUserId)).thenReturn(Collections.emptyList());

        List<ProjectSimpleResponse> responses = projectService.getProjectsByUserId(requestingUserId);

        assertThat(responses).isEmpty();
        verify(projectRepository, times(1)).findAllByUserId(requestingUserId);
    }

    @Test
    void testGetProjectDetailInfoById() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectEntity projectEntity = spy(new ProjectEntity("Test Project", ProjectState.ACTIVE, LocalDateTime.now()));
        when(projectEntity.getId()).thenReturn(projectId);

        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);

        ProjectDetailResponse response = projectService.getProjectDetailInfoById(requestingUserId, projectId);

        assertThat(response.id()).isEqualTo(projectId);
        assertThat(response.name()).isEqualTo("Test Project");
        verify(businessRuleValidator, times(1)).findProjectOrThrow(projectId);
        verify(businessRuleValidator, times(1)).validateProjectMembership(requestingUserId, projectId);
    }

    @Test
    void testUpdateProject() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectUpdateRequest request = new ProjectUpdateRequest("Updated Name", "DORMANT");
        ProjectEntity projectEntity = new ProjectEntity("Old Name", ProjectState.ACTIVE);

        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectAdmin(requestingUserId, projectId);

        projectService.updateProject(requestingUserId, projectId, request);

        assertThat(projectEntity.getName()).isEqualTo("Updated Name");
        assertThat(projectEntity.getState()).isEqualTo(ProjectState.DORMANT);
        verify(projectRepository, times(1)).save(projectEntity);
    }

    @Test
    void testDeleteProject() {
        Long requestingUserId = 1L;
        Long projectId = 1L;

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectAdmin(requestingUserId, projectId);

        projectService.deleteProject(requestingUserId, projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }
}
