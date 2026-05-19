package com.nhnacademy.task.service;


import com.nhnacademy.task.dto.req.TaskCreateRequest;
import com.nhnacademy.task.dto.req.TaskUpdateRequest;
import com.nhnacademy.task.dto.resp.TaskDetailResponse;
import com.nhnacademy.task.dto.resp.TaskSimpleResponse;

import java.util.List;

public interface TaskService {
    //create
    void createTask(Long requestingUserId, Long projectId, TaskCreateRequest taskCreateRequest);

    //read
    TaskSimpleResponse getTaskSimpleInfoById(Long requestingUserId, Long projectId, Long taskId);
    TaskDetailResponse getTaskDetailInfoById(Long requestingUserId, Long projectId, Long taskId);
    List<TaskSimpleResponse> getTasksByProjectID(Long requestingUserId, Long projectId);

    //update
    void updateTask(Long requestingUserId, Long projectId, Long taskId, TaskUpdateRequest taskUpdateRequest);

    //delete
    void deleteTask(Long requestingUserId, Long projectId, Long taskId);
}
