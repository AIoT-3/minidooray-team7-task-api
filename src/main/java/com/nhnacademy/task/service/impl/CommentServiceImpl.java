package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.entity.CommentEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.repository.CommentRepository;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.TaskRepository;
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
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    @Override
    public CommentResponse createComment(Long taskId, Long projectMemberId, String content) {

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        ProjectMemberEntity projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new EntityNotFoundException("Project member not found"));


        CommentEntity comment = new CommentEntity(task, projectMember, content);
        task.addComment(comment);
        projectMember.addComment(comment);
        CommentEntity saved = commentRepository.save(comment);

        return toResponse(saved);
    }

    @Override
    public List<CommentResponse> getComments(Long projectId, Long taskId) {

        if (!taskRepository.existsByProject_IdAndId(projectId, taskId)) {
            throw new EntityNotFoundException("Task not found");
        }

        return commentRepository.findAllByTask_Id(taskId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long projectId, Long taskId, Long commentId, Long projectMemberId, String content) {

        projectMemberRepository.findByProject_IdAndUserId(projectMemberId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project member not found"));
        taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        CommentEntity comment = commentRepository.findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!projectMemberId.equals(comment.getProjectMember().getId())) {
            throw new NoPermissionException("이 댓글을 수정할 권한이 없습니다.");
        }

        comment.updateContent(content);

        return toResponse(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long projectId, Long taskId, Long commentId, Long projectMemberId) {
        CommentEntity comment = commentRepository.findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!projectId.equals(comment.getTask().getProject().getId())) {
            throw new EntityNotFoundException("Task not found");
        }

        if (!projectMemberId.equals(comment.getProjectMember().getId())) {
            throw new NoPermissionException("이 댓글을 삭제할 권한이 없습니다.");
        }

        comment.getTask().removeComment(comment);
        comment.getProjectMember().removeComment(comment);

        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(CommentEntity comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getProjectMember().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
