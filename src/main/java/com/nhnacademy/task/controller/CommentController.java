package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.CommentCreateRequest;
import com.nhnacademy.task.dto.req.CommentUpdateRequest;
import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CommentController
 *
 * @author chosun-nhn12
 * @since 26. 5. 19.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks/{task-id}/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentResponse> getComments(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId) {
        return commentService.getComments(projectId, taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @RequestHeader("X-USER-ID") Long projectMemberId,
            @Valid @RequestBody CommentCreateRequest request
            ) {
        return commentService.createComment(taskId, projectMemberId, request.content());
    }

    @PutMapping("/{comment-id}")
    public CommentResponse modifyComment(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @PathVariable("comment-id") Long commentId,
            @RequestHeader("X-USER-ID") Long projectMemberId,
            @Valid @RequestBody CommentUpdateRequest request
            ) {
        return commentService.updateComment(projectId, taskId, commentId, projectMemberId, request.content());
    }

    @DeleteMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable("project-id") Long projectId,
            @PathVariable("task-id") Long taskId,
            @PathVariable("comment-id") Long commentId,
            @RequestHeader("X-USER-ID") Long projectMemberId
    ) {
        commentService.deleteComment(projectId, taskId, commentId, projectMemberId);
    }
}
