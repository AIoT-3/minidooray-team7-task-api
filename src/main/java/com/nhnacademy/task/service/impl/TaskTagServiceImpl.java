package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.entity.TaskTagEntity;
import com.nhnacademy.task.service.TaskTagService;

/**
 * TaskTagServiceImpl
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
public class TaskTagServiceImpl implements TaskTagService {
    @Override
    public TaskTagEntity createTaskTag(TagEntity tag, TaskEntity task) {
        TaskTagEntity taskTag = new TaskTagEntity(tag, task);
        tag.addTaskTag(taskTag);
        task.addTaskTag(taskTag);

        return taskTag;
    }
}
