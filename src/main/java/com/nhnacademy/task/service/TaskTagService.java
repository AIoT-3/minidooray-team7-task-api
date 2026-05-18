package com.nhnacademy.task.service;

import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.entity.TaskTagEntity;

/**
 * TaskTagService
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
public interface TaskTagService {
    TaskTagEntity createTaskTag(TagEntity tag, TaskEntity task);
}
