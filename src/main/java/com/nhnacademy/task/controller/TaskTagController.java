package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.TaskTagCreateRequest;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.service.TaskTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskTagController
 *
 * @author chosun-nhn12
 * @since 26. 5. 19.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks/{task-id}/tags")
public class TaskTagController {
    private final TaskTagService taskTagService;


    @GetMapping
    public List<TagResponse> getTaskTags(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId
    ) {
        return taskTagService.getTaskTags(projectId, taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<TagResponse> attachTaskTags(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @Valid @RequestBody TaskTagCreateRequest request
            ) {
        List<TagResponse> attachedTags = new ArrayList<>();

        for(Long tagId : request.tagIds()) {
            attachedTags.add(taskTagService.attachTag(projectId, taskId, tagId));
        }
        return attachedTags;
    }

    @DeleteMapping("/{tag-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void detachTaskTag(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @PathVariable("tag-id") Long tagId
    ) {
        taskTagService.detachTag(projectId, taskId, tagId);
    }
}
