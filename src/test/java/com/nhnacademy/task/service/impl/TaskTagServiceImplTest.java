package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityAlreadyExistsException;
import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.*;
import com.nhnacademy.task.repository.TagRepository;
import com.nhnacademy.task.repository.TaskRepository;
import com.nhnacademy.task.repository.TaskTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * TaskTagServiceImplTest
 *
 * @author chosun-nhn12
 * @since 26. 5. 22.
 */
@ExtendWith(MockitoExtension.class)
class TaskTagServiceImplTest {
    @Mock
    TaskTagRepository taskTagRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskTagServiceImpl taskTagService;


    @Test
    void attachTag() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;

        String tagName = "testTag";

        TaskEntity task = new TaskEntity("taskName", null, null, null);

        TagEntity tag = new TagEntity(
                tagId,
                new ProjectEntity("testProject", ProjectState.ACTIVE),
                tagName,
                LocalDateTime.now(),
                new ArrayList<>()
        );

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));

        given(tagRepository.findByIdAndProject_Id(tagId, projectId))
                .willReturn(Optional.of(tag));

        given(taskTagRepository.existsByTag_IdAndTask_Id(tagId, taskId))
                .willReturn(false);


        TagResponse response =  taskTagService.attachTag(projectId, taskId, tagId);

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(taskTagRepository).should().existsByTag_IdAndTask_Id(tagId, taskId);
        then(taskTagRepository).should().save(any(TaskTagEntity.class));

        assertEquals(tagId, response.id());
        assertEquals(tagName, response.name());
    }

    @Test
    void attachTagButTaskNotFound() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;

        given(taskRepository.findById(taskId))
                .willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                ()-> taskTagService.attachTag(projectId, taskId, tagId)
        );

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should(never()).findByIdAndProject_Id(tagId, projectId);
    }

    @Test
    void attachTagButTagNotFound() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;

        TaskEntity task = new TaskEntity("taskName", null, null, null);

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));

        given(tagRepository.findByIdAndProject_Id(tagId, projectId))
                .willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskTagService.attachTag(projectId, taskId, tagId)
        );

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(taskTagRepository).should(never()).existsByTag_IdAndTask_Id(tagId, taskId);
    }

    @Test
    void attachTagButTagAlreadyExistsForTask() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;

        String tagName = "testTag";

        TaskEntity task = new TaskEntity("taskName", null, null, null);

        TagEntity tag = new TagEntity(
                tagId,
                new ProjectEntity("testProject", ProjectState.ACTIVE),
                tagName,
                LocalDateTime.now(),
                new ArrayList<>()
        );

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));

        given(tagRepository.findByIdAndProject_Id(tagId, projectId))
                .willReturn(Optional.of(tag));

        given(taskTagRepository.existsByTag_IdAndTask_Id(tagId, taskId))
                .willReturn(true);

        assertThrows(EntityAlreadyExistsException.class,
                () -> taskTagService.attachTag(projectId, taskId, tagId)
        );

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(taskTagRepository).should().existsByTag_IdAndTask_Id(tagId, taskId);
        then(taskTagRepository).should(never()).save(any());
    }

    @Test
    void attachTagButTaskDoesNotBelongToProject() {
        // task.project.id != projectId 인 상황
        // 현재 구현은 예외 없이 attach될 가능성이 있음
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;


    }

    @Test
    void getTaskTags() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;
        Long taskTagId = 10L;

        String tagName = "testTag";

        ProjectEntity project = new ProjectEntity("testProject", ProjectState.ACTIVE);

        TagEntity tag = new TagEntity(
                tagId,
                project,
                tagName,
                LocalDateTime.now(),
                new ArrayList<>()
        );

        TaskEntity task = new TaskEntity(
                "taskName",
                project,
                new ProjectMemberEntity(project, 1L, ProjectMemberRole.ADMIN),
                "taskContent"
        );

        TaskTagEntity taskTag = new TaskTagEntity(
                taskTagId,
                tag,
                task
        );

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));

        given(taskTagRepository.findAllByTask_IdAndTask_Project_Id(taskId, projectId))
                .willReturn(List.of(taskTag));

        List<TagResponse> tagResponses = taskTagService.getTaskTags(projectId, taskId);

        assertTrue(!tagResponses.isEmpty());

        then(taskRepository).should().findById(taskId);
        then(taskTagRepository).should().findAllByTask_IdAndTask_Project_Id(taskId, projectId);
        assertEquals(tagId, tagResponses.getFirst().id());
        assertEquals(tagName, tagResponses.getFirst().name());
    }

    @Test
    void getTaskTagButTaskNotFound() {
        Long projectId = 2L;
        Long taskId = 3L;

        given(taskRepository.findById(taskId))
                .willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> taskTagService.getTaskTags(projectId, taskId));

        then(taskRepository).should().findById(taskId);
        then(taskTagRepository).should(never()).findAllByTask_IdAndTask_Project_Id(taskId, projectId);
    }

    @Test
    void detachTag() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;
        Long taskTagId = 10L;

        String tagName = "testTag";

        ProjectEntity project = new ProjectEntity("testProject", ProjectState.ACTIVE);

        TagEntity tag = new TagEntity(
                tagId,
                project,
                tagName,
                LocalDateTime.now(),
                new ArrayList<>()
        );

        TaskEntity task = new TaskEntity(
                "taskName",
                project,
                new ProjectMemberEntity(project, 1L, ProjectMemberRole.ADMIN),
                "taskContent"
        );

        TaskTagEntity taskTag = new TaskTagEntity(
                taskTagId,
                tag,
                task
        );

        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(tagRepository.findByIdAndProject_Id(tagId, projectId)).willReturn(Optional.of(tag));
        given(taskTagRepository.findByTag_IdAndTask_IdAndTask_Project_Id(tagId, taskId, projectId))
                .willReturn(Optional.of(taskTag));

        taskTagService.detachTag(projectId, taskId, tagId);

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(taskTagRepository).should().findByTag_IdAndTask_IdAndTask_Project_Id(tagId, taskId, projectId);
        then(taskTagRepository).should().delete(taskTag);
    }

    @Test
    void detachTagButTaskNotFound() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;

        given(taskRepository.findById(taskId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskTagService.detachTag(projectId, taskId, tagId));

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should(never()).findByIdAndProject_Id(tagId, projectId);
    }

    @Test
    void detachTagButTagNotFound() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;

        TaskEntity task = new TaskEntity("taskName", null, null, null);

        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(tagRepository.findByIdAndProject_Id(tagId, projectId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskTagService.detachTag(projectId, taskId, tagId));

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(taskTagRepository).should(never())
                .findByTag_IdAndTask_IdAndTask_Project_Id(tagId, taskId, projectId);
    }

    @Test
    void detachTagButTaskTagNotFound() {
        Long projectId = 2L;
        Long taskId = 3L;
        Long tagId = 1L;

        TaskEntity task = new TaskEntity("taskName", null, null, null);
        TagEntity tag = new TagEntity(null, "tagName");

        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(tagRepository.findByIdAndProject_Id(tagId, projectId)).willReturn(Optional.of(tag));
        given(taskTagRepository.findByTag_IdAndTask_IdAndTask_Project_Id(tagId, taskId, projectId))
                .willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskTagService.detachTag(projectId, taskId, tagId));

        then(taskRepository).should().findById(taskId);
        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(taskTagRepository).should().findByTag_IdAndTask_IdAndTask_Project_Id(tagId, taskId, projectId);
        then(taskTagRepository).should(never()).delete(any());
    }
}