package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityAlreadyExistsException;
import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.entity.TaskTagEntity;
import com.nhnacademy.task.repository.TagRepository;
import com.nhnacademy.task.repository.TaskRepository;
import com.nhnacademy.task.repository.TaskTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskTagServiceImplTest {

    @InjectMocks
    private TaskTagServiceImpl taskTagService;

    @Mock
    private TaskTagRepository taskTagRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAttachTag() {
        Long projectId = 1L;
        Long taskId = 1L;
        Long tagId = 1L;
        TaskEntity task = new TaskEntity("Task", null, null, "Content");
        TagEntity tag = spy(new TagEntity(null, "Tag"));
        when(tag.getId()).thenReturn(tagId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(tagRepository.findByIdAndProject_Id(tagId, projectId)).thenReturn(Optional.of(tag));
        when(taskTagRepository.existsByTag_IdAndTask_Id(tagId, taskId)).thenReturn(false);

        TagResponse response = taskTagService.attachTag(projectId, taskId, tagId);

        assertThat(response.id()).isEqualTo(tagId);
        assertThat(response.name()).isEqualTo("Tag");
        verify(taskTagRepository, times(1)).save(any(TaskTagEntity.class));
        assertThat(tag.getTaskTagList()).hasSize(1);
        assertThat(task.getTaskTagList()).hasSize(1);
    }

    @Test
    void testAttachTag_AlreadyExists() {
        Long projectId = 1L;
        Long taskId = 1L;
        Long tagId = 1L;
        TaskEntity task = new TaskEntity("Task", null, null, "Content");
        TagEntity tag = new TagEntity(null, "Tag");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(tagRepository.findByIdAndProject_Id(tagId, projectId)).thenReturn(Optional.of(tag));
        when(taskTagRepository.existsByTag_IdAndTask_Id(tagId, taskId)).thenReturn(true);

        assertThatThrownBy(() -> taskTagService.attachTag(projectId, taskId, tagId))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("tag already exists for task");
    }

    @Test
    void testGetTaskTags() {
        Long projectId = 1L;
        Long taskId = 1L;
        TaskEntity task = new TaskEntity("Task", null, null, "Content");
        TagEntity tag = spy(new TagEntity(null, "Tag"));
        when(tag.getId()).thenReturn(10L);
        TaskTagEntity taskTag = new TaskTagEntity(tag, task);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskTagRepository.findAllByTask_IdAndTask_Project_Id(taskId, projectId)).thenReturn(List.of(taskTag));

        List<TagResponse> responses = taskTagService.getTaskTags(projectId, taskId);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).id()).isEqualTo(10L);
        assertThat(responses.get(0).name()).isEqualTo("Tag");
    }

    @Test
    void testDetachTag() {
        Long projectId = 1L;
        Long taskId = 1L;
        Long tagId = 1L;
        TaskEntity task = new TaskEntity("Task", null, null, "Content");
        TagEntity tag = new TagEntity(null, "Tag");
        TaskTagEntity taskTag = new TaskTagEntity(tag, task);
        
        task.addTaskTag(taskTag);
        tag.addTaskTag(taskTag);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(tagRepository.findByIdAndProject_Id(tagId, projectId)).thenReturn(Optional.of(tag));
        when(taskTagRepository.findByTag_IdAndTask_IdAndTask_Project_Id(tagId, taskId, projectId)).thenReturn(Optional.of(taskTag));

        taskTagService.detachTag(projectId, taskId, tagId);

        assertThat(tag.getTaskTagList()).isEmpty();
        assertThat(task.getTaskTagList()).isEmpty();
        verify(taskTagRepository, times(1)).delete(taskTag);
    }
}
