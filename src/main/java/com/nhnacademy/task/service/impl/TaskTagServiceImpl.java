package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityAlreadyExistsException;
import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.entity.TaskTagEntity;
import com.nhnacademy.task.repository.TagRepository;
import com.nhnacademy.task.repository.TaskTagRepository;
import com.nhnacademy.task.repository.TaskRepository;
import com.nhnacademy.task.service.TaskTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskTagServiceImpl
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskTagServiceImpl implements TaskTagService {
    private final TaskTagRepository taskTagRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    @Transactional
    @Override
    public TagResponse attachTag(Long projectId, Long taskId, Long tagId) {
        TaskEntity task = taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        TagEntity tag = tagRepository.findByIdAndProject_Id(tagId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        if (taskTagRepository.existsByTag_IdAndTask_Id(tagId, taskId)) {
            throw new EntityAlreadyExistsException("tag already exists for task");
        }

        TaskTagEntity taskTag = new TaskTagEntity(tag, task);
        tag.addTaskTag(taskTag);
        task.addTaskTag(taskTag);
        taskTagRepository.save(taskTag);

        return toResponse(tag);
    }

    @Override
    public List<TagResponse> getTaskTags(Long projectId, Long taskId) {
        taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        List<TaskTagEntity> taskTags = taskTagRepository.findAllByTask_IdAndTask_Project_Id(taskId, projectId);
        List<TagResponse> responses = new ArrayList<>();

        for (TaskTagEntity taskTag : taskTags) {
            responses.add(toResponse(taskTag.getTag()));
        }

        return responses;
    }

    @Override
    @Transactional
    public void detachTag(Long projectId, Long taskId, Long tagId) {
        taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        tagRepository.findByIdAndProject_Id(tagId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        TaskTagEntity taskTag = taskTagRepository.findByTag_IdAndTask_IdAndTask_Project_Id(tagId, taskId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Task tag not found"));

        taskTagRepository.delete(taskTag);
    }

    private TagResponse toResponse(TagEntity tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
