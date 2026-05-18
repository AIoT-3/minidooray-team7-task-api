package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.entity.CommentEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.TaskEntity;

import java.util.List;

/**
 * CommentService
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
public interface CommentService {
    CommentEntity create(TaskEntity task, ProjectMemberEntity projectMember, String content);

    List<CommentResponse> getComments(Long taskEntityId);
}
