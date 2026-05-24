package com.nhnacademy.task.common.validator;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.ExternalApiException;
import com.nhnacademy.task.common.exception.InvalidRequestException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.dto.resp.UserResponse;
import com.nhnacademy.task.entity.*;
import com.nhnacademy.task.repository.MilestoneRepository;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BusinessRuleValidatorTest {

    @InjectMocks
    private BusinessRuleValidator businessRuleValidator;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMemberRepository projectMemberRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private MilestoneRepository milestoneRepository;
    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ConvertibleClientHttpResponse convertibleClientHttpResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
    }

    @Nested
    @DisplayName("findUserOrExists Tests")
    class FindUserOrExistsTests {
        @Test
        @DisplayName("Success: User found")
        void findUserOrExists_Success() throws Exception {
            UserResponse mockUser = new UserResponse(1L, "test", "test@test.com", null, null);
            when(requestHeadersSpec.exchange(any(ExchangeFunction.class))).thenAnswer(invocation -> {
                when(convertibleClientHttpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
                when(convertibleClientHttpResponse.bodyTo(UserResponse.class)).thenReturn(mockUser);
                return invocation.getArgument(0, ExchangeFunction.class)
                        .exchange(null, convertibleClientHttpResponse);
            });

            UserResponse user = businessRuleValidator.findUserOrExists(1L);

            assertThat(user).isNotNull();
            assertThat(user.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Failure: User not found (404)")
        void findUserOrExists_NotFound() throws Exception {
            when(requestHeadersSpec.exchange(any(ExchangeFunction.class))).thenAnswer(invocation -> {
                when(convertibleClientHttpResponse.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
                return invocation.getArgument(0, ExchangeFunction.class)
                        .exchange(null, convertibleClientHttpResponse);
            });

            assertThatThrownBy(() -> businessRuleValidator.findUserOrExists(1L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("user not found");
        }

        @Test
        @DisplayName("Failure: External API error")
        void findUserOrExists_ApiError() throws Exception {
            String errorBody = "Internal Server Error";
            when(requestHeadersSpec.exchange(any(ExchangeFunction.class))).thenAnswer(invocation -> {
                when(convertibleClientHttpResponse.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
                when(convertibleClientHttpResponse.bodyTo(String.class)).thenReturn(errorBody);
                return invocation.getArgument(0, ExchangeFunction.class)
                        .exchange(null, convertibleClientHttpResponse);
            });

            assertThatThrownBy(() -> businessRuleValidator.findUserOrExists(1L))
                    .isInstanceOf(ExternalApiException.class)
                    .hasMessageContaining(errorBody);
        }
    }

    @Nested
    @DisplayName("Entity Finder Tests")
    class EntityFinderTests {
        @Test
        void findProjectOrThrow_Success() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(mock(ProjectEntity.class)));
            assertThatCode(() -> businessRuleValidator.findProjectOrThrow(1L)).doesNotThrowAnyException();
        }

        @Test
        void findProjectOrThrow_Failure() {
            when(projectRepository.findById(1L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> businessRuleValidator.findProjectOrThrow(1L))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void findProjectMemberOrThrow_ById_Success() {
            when(projectMemberRepository.findById(1L)).thenReturn(Optional.of(mock(ProjectMemberEntity.class)));
            assertThatCode(() -> businessRuleValidator.findProjectMemberOrThrow(1L)).doesNotThrowAnyException();
        }

        @Test
        void findProjectMemberOrThrow_ById_Failure() {
            when(projectMemberRepository.findById(1L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> businessRuleValidator.findProjectMemberOrThrow(1L))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void findProjectMemberOrThrow_ByProjectIdAndUserId_Success() {
            when(projectMemberRepository.findByProject_IdAndUserId(1L, 2L)).thenReturn(Optional.of(mock(ProjectMemberEntity.class)));
            assertThatCode(() -> businessRuleValidator.findProjectMemberOrThrow(1L, 2L)).doesNotThrowAnyException();
        }

        @Test
        void findProjectMemberOrThrow_ByProjectIdAndUserId_Failure() {
            when(projectMemberRepository.findByProject_IdAndUserId(1L, 2L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> businessRuleValidator.findProjectMemberOrThrow(1L, 2L))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void findTaskOrThrow_Success() {
            when(taskRepository.findById(1L)).thenReturn(Optional.of(mock(TaskEntity.class)));
            assertThatCode(() -> businessRuleValidator.findTaskOrThrow(1L)).doesNotThrowAnyException();
        }

        @Test
        void findTaskOrThrow_Failure() {
            when(taskRepository.findById(1L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> businessRuleValidator.findTaskOrThrow(1L))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void findMilestoneOrThrow_Success() {
            when(milestoneRepository.findById(1L)).thenReturn(Optional.of(mock(MilestoneEntity.class)));
            assertThatCode(() -> businessRuleValidator.findMilestoneOrThrow(1L)).doesNotThrowAnyException();
        }

        @Test
        void findMilestoneOrThrow_Failure() {
            when(milestoneRepository.findById(1L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> businessRuleValidator.findMilestoneOrThrow(1L))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Validation Logic Tests")
    class ValidationLogicTests {
        @Test
        void validateProjectExists_Success() {
            when(projectRepository.existsById(1L)).thenReturn(true);
            assertThatCode(() -> businessRuleValidator.validateProjectExists(1L)).doesNotThrowAnyException();
        }

        @Test
        void validateProjectExists_Failure() {
            when(projectRepository.existsById(1L)).thenReturn(false);
            assertThatThrownBy(() -> businessRuleValidator.validateProjectExists(1L))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void validateProjectMembership_Success() {
            when(projectMemberRepository.existsByUserIdAndProject_Id(1L, 1L)).thenReturn(true);
            assertThatCode(() -> businessRuleValidator.validateProjectMembership(1L, 1L)).doesNotThrowAnyException();
        }

        @Test
        void validateProjectMembership_Failure() {
            when(projectMemberRepository.existsByUserIdAndProject_Id(1L, 1L)).thenReturn(false);
            assertThatThrownBy(() -> businessRuleValidator.validateProjectMembership(1L, 1L))
                    .isInstanceOf(NoPermissionException.class);
        }

        @Test
        void validateProjectAdmin_Success() {
            when(projectMemberRepository.existsByProject_IdAndUserIdAndRole(1L, 1L, ProjectMemberRole.ADMIN)).thenReturn(true);
            assertThatCode(() -> businessRuleValidator.validateProjectAdmin(1L, 1L)).doesNotThrowAnyException();
        }

        @Test
        void validateProjectAdmin_Failure() {
            when(projectMemberRepository.existsByProject_IdAndUserIdAndRole(1L, 1L, ProjectMemberRole.ADMIN)).thenReturn(false);
            assertThatThrownBy(() -> businessRuleValidator.validateProjectAdmin(1L, 1L))
                    .isInstanceOf(NoPermissionException.class);
        }

        @Test
        void validateMemberIsNotAdmin_Success() {
            when(projectMemberRepository.existsByIdAndProject_IdAndRole(1L, 1L, ProjectMemberRole.ADMIN)).thenReturn(false);
            assertThatCode(() -> businessRuleValidator.validateMemberIsNotAdmin(1L, 1L)).doesNotThrowAnyException();
        }

        @Test
        void validateMemberIsNotAdmin_Failure() {
            when(projectMemberRepository.existsByIdAndProject_IdAndRole(1L, 1L, ProjectMemberRole.ADMIN)).thenReturn(true);
            assertThatThrownBy(() -> businessRuleValidator.validateMemberIsNotAdmin(1L, 1L))
                    .isInstanceOf(NoPermissionException.class);
        }

        @Test
        void validateMemberBelongsToProject_Success() {
            when(projectMemberRepository.existsByIdAndProject_Id(1L, 1L)).thenReturn(true);
            assertThatCode(() -> businessRuleValidator.validateMemberBelongsToProject(1L, 1L)).doesNotThrowAnyException();
        }

        @Test
        void validateMemberBelongsToProject_Failure() {
            when(projectMemberRepository.existsByIdAndProject_Id(1L, 1L)).thenReturn(false);
            assertThatThrownBy(() -> businessRuleValidator.validateMemberBelongsToProject(1L, 1L))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        void validateTaskBelongsToProject_Success() {
            when(taskRepository.existsByProject_IdAndId(1L, 1L)).thenReturn(true);
            assertThatCode(() -> businessRuleValidator.validateTaskBelongsToProject(1L, 1L)).doesNotThrowAnyException();
        }

        @Test
        void validateTaskBelongsToProject_Failure() {
            when(taskRepository.existsByProject_IdAndId(1L, 1L)).thenReturn(false);
            assertThatThrownBy(() -> businessRuleValidator.validateTaskBelongsToProject(1L, 1L))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        void validateMilestoneBelongsToProject_Success() {
            when(milestoneRepository.existsByProject_IdAndId(1L, 1L)).thenReturn(true);
            assertThatCode(() -> businessRuleValidator.validateMilestoneBelongsToProject(1L, 1L)).doesNotThrowAnyException();
        }

        @Test
        void validateMilestoneBelongsToProject_Failure() {
            when(milestoneRepository.existsByProject_IdAndId(1L, 1L)).thenReturn(false);
            assertThatThrownBy(() -> businessRuleValidator.validateMilestoneBelongsToProject(1L, 1L))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        void validateMilestoneBelongsToTask_Success() {
            when(taskRepository.existsByMilestone_IdAndId(1L, 1L)).thenReturn(true);
            assertThatCode(() -> businessRuleValidator.validateMilestoneBelongsToTask(1L, 1L)).doesNotThrowAnyException();
        }

        @Test
        void validateMilestoneBelongsToTask_Failure() {
            when(taskRepository.existsByMilestone_IdAndId(1L, 1L)).thenReturn(false);
            assertThatThrownBy(() -> businessRuleValidator.validateMilestoneBelongsToTask(1L, 1L))
                    .isInstanceOf(InvalidRequestException.class);
        }
        
        @Test
        void validateStartAndEndDates_Success() {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusDays(1);
            assertThatCode(() -> businessRuleValidator.validateStartAndEndDates(start, end)).doesNotThrowAnyException();
        }

        @Test
        void validateStartAndEndDates_Failure() {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.minusDays(1);
            assertThatThrownBy(() -> businessRuleValidator.validateStartAndEndDates(start, end))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }
}
