package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.TaskCreateRequest;
import com.nhnacademy.task.dto.req.TaskMileStoneCreateRequest;
import com.nhnacademy.task.dto.req.TaskUpdateRequest;
import com.nhnacademy.task.dto.resp.TaskDetailResponse;
import com.nhnacademy.task.dto.resp.TaskSimpleResponse;
import com.nhnacademy.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Void> createTask(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @Valid @RequestBody TaskCreateRequest request
    ) {
        taskService.createTask(requestingUserId, projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<TaskSimpleResponse>> getTasksByProject(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId
    ) {
        List<TaskSimpleResponse> tasks = taskService.getTasksByProjectID(requestingUserId, projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponse> getTaskById(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "taskId") Long taskId
    ){
        TaskDetailResponse response = taskService.getTaskDetailInfoById(requestingUserId, projectId, taskId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "taskId") Long taskId,
            @Valid @RequestBody TaskUpdateRequest request
    ) {
        taskService.updateTask(requestingUserId,projectId,taskId,request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}/milestones")
    public ResponseEntity<Void> addMilestoneToTask(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "taskId") Long taskId,
            @Valid @RequestBody TaskMileStoneCreateRequest taskMileStoneCreateRequest
            ){
        taskService.addMilestoneToTask(requestingUserId,projectId,taskId,taskMileStoneCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}/milestones/{milestoneId}")
    public ResponseEntity<Void> removeMilestoneOnTask(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "taskId") Long taskId,
            @PathVariable(name = "milestoneId") Long milestoneId
    ){
        taskService.removeMilestoneOnTask(requestingUserId,projectId,taskId,milestoneId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "taskId") Long taskId
    ){
        taskService.deleteTask(requestingUserId,projectId,taskId);
        return ResponseEntity.noContent().build();
    }
}
