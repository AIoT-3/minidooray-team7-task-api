package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.validator.BusinessRuleValidator;
import com.nhnacademy.task.dto.req.ProjectMemberCreateRequest;
import com.nhnacademy.task.dto.resp.ProjectMemberResponse;
import com.nhnacademy.task.dto.resp.UserResponse;
import com.nhnacademy.task.dto.resp.UserStatus;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.ProjectMemberRole;
import com.nhnacademy.task.entity.ProjectState;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectMemberServiceImplTest {

    @InjectMocks
    private ProjectMemberServiceImpl projectMemberService;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private BusinessRuleValidator businessRuleValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProjectMember_NewMember() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long newUserId = 2L;
        ProjectMemberCreateRequest request = new ProjectMemberCreateRequest(newUserId);
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        UserResponse userResponse = new UserResponse(newUserId, "newUser", "email", UserStatus.ACTIVATE, LocalDateTime.now());

        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectAdmin(requestingUserId, projectId);
        when(businessRuleValidator.findUserOrExists(newUserId)).thenReturn(userResponse);
        when(projectMemberRepository.findByProject_IdAndUserId(projectId, newUserId)).thenReturn(Optional.empty());

        projectMemberService.createProjectMember(requestingUserId, projectId, request);

        verify(projectMemberRepository, times(1)).save(any(ProjectMemberEntity.class));
    }

    @Test
    void testCreateProjectMember_ExistingMember() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long existingUserId = 2L;
        ProjectMemberCreateRequest request = new ProjectMemberCreateRequest(existingUserId);
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        ProjectMemberEntity existingMember = new ProjectMemberEntity(projectEntity, existingUserId, ProjectMemberRole.MEMBER);
        existingMember.markAsDeleted();
        UserResponse userResponse = new UserResponse(existingUserId, "existingUser", "email", UserStatus.ACTIVATE, LocalDateTime.now());

        when(businessRuleValidator.findProjectOrThrow(projectId)).thenReturn(projectEntity);
        doNothing().when(businessRuleValidator).validateProjectAdmin(requestingUserId, projectId);
        when(businessRuleValidator.findUserOrExists(existingUserId)).thenReturn(userResponse);
        when(projectMemberRepository.findByProject_IdAndUserId(projectId, existingUserId)).thenReturn(Optional.of(existingMember));

        projectMemberService.createProjectMember(requestingUserId, projectId, request);

        assertThat(existingMember.getIsDeleted()).isFalse();
        verify(projectMemberRepository, times(1)).save(existingMember);
    }

    @Test
    void testGetById() {
        Long requestingUserId = 1L;
        Long projectMemberId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = spy(new ProjectMemberEntity(projectEntity, 2L, ProjectMemberRole.MEMBER));
        
        when(projectEntity.getId()).thenReturn(10L);
        when(projectMemberEntity.getId()).thenReturn(projectMemberId);
        when(businessRuleValidator.findProjectMemberOrThrow(projectMemberId)).thenReturn(projectMemberEntity);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, 10L);

        ProjectMemberResponse response = projectMemberService.getById(requestingUserId, projectMemberId);

        assertThat(response.id()).isEqualTo(projectMemberId);
        assertThat(response.userId()).isEqualTo(2L);
    }

    @Test
    void testGetByUserIdAndProjectId() {
        Long requestingUserId = 1L;
        Long userId = 2L;
        Long projectId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = spy(new ProjectMemberEntity(projectEntity, userId, ProjectMemberRole.MEMBER));
        when(projectMemberEntity.getId()).thenReturn(100L);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(businessRuleValidator.findProjectMemberOrThrow(projectId, userId)).thenReturn(projectMemberEntity);

        ProjectMemberResponse response = projectMemberService.getByUserIdAndProjectId(requestingUserId, userId, projectId);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.userId()).isEqualTo(userId);
    }

    @Test
    void testGetAllByProjectId() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        ProjectMemberEntity member1 = spy(new ProjectMemberEntity(projectEntity, 2L, ProjectMemberRole.MEMBER));
        ProjectMemberEntity member2 = spy(new ProjectMemberEntity(projectEntity, 3L, ProjectMemberRole.ADMIN));
        when(member1.getId()).thenReturn(100L);
        when(member2.getId()).thenReturn(200L);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectMembership(requestingUserId, projectId);
        when(projectMemberRepository.findAllByProject_Id(projectId)).thenReturn(List.of(member1, member2));

        List<ProjectMemberResponse> responses = projectMemberService.getAllByProjectId(requestingUserId, projectId);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(100L);
        assertThat(responses.get(1).id()).isEqualTo(200L);
    }

    @Test
    void testDeleteProjectMemberByUserIdAndProjectId() {
        Long requestingUserId = 1L;
        Long userId = 2L;
        Long projectId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = spy(new ProjectMemberEntity(projectEntity, userId, ProjectMemberRole.MEMBER));
        when(projectMemberEntity.getId()).thenReturn(100L);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectAdmin(requestingUserId, projectId);
        when(businessRuleValidator.findProjectMemberOrThrow(projectId, userId)).thenReturn(projectMemberEntity);
        doNothing().when(businessRuleValidator).validateMemberIsNotAdmin(projectId, 100L);

        projectMemberService.deleteProjectMemberByUserIdAndProjectId(requestingUserId, userId, projectId);

        assertThat(projectMemberEntity.getIsDeleted()).isTrue();
    }

    @Test
    void testDeleteProjectMemberByProjectIdAndProjectMemberId() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long projectMemberId = 1L;
        ProjectEntity projectEntity = mock(ProjectEntity.class);
        ProjectMemberEntity projectMemberEntity = new ProjectMemberEntity(projectEntity, 2L, ProjectMemberRole.MEMBER);

        doNothing().when(businessRuleValidator).validateProjectExists(projectId);
        doNothing().when(businessRuleValidator).validateProjectAdmin(requestingUserId, projectId);
        when(businessRuleValidator.findProjectMemberOrThrow(projectMemberId)).thenReturn(projectMemberEntity);
        doNothing().when(businessRuleValidator).validateMemberBelongsToProject(projectId, projectMemberId);
        doNothing().when(businessRuleValidator).validateMemberIsNotAdmin(projectId, projectMemberId);

        projectMemberService.deleteProjectMemberByProjectIdAndProjectMemberId(requestingUserId, projectId, projectMemberId);

        assertThat(projectMemberEntity.getIsDeleted()).isTrue();
    }
}
