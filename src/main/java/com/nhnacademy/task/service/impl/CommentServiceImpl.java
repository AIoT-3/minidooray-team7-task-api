package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.entity.CommentEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.repository.CommentRepository;
import com.nhnacademy.task.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CommentService
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public CommentEntity create(TaskEntity task, ProjectMemberEntity projectMember, String content) {
        CommentEntity comment = new CommentEntity(task, projectMember, content);
        task.addComment(comment);
        projectMember.addComment(comment);

        return comment;
    }

    public List<CommentResponse> getComments(Long taskId) {
        return commentRepository.findAllByTask_Id(taskId);
    }
}
