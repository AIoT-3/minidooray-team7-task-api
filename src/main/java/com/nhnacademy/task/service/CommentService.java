package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.resp.CommentResponse;

import java.util.List;

/**
 * CommentService
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
public interface CommentService {
    CommentResponse createComment(Long taskId, Long projectMemberId, String content);

    List<CommentResponse> getComments(Long projectId, Long taskId);

    CommentResponse updateComment(Long projectId, Long taskId, Long commentId, Long projectMemberId, String content);

    void deleteComment(Long projectId, Long taskId, Long commentId, Long projectMemberId);
}
