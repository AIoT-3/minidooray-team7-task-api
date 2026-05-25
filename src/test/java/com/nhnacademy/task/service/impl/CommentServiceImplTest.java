package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.entity.*;
import com.nhnacademy.task.repository.CommentRepository;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * CommentServiceImplTest
 *
 * @author chosun-nhn12
 * @since 26. 5. 22.
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    private Long taskId;
    private Long projectId;
    private Long projectMemberId;
    private Long commentId;
    private String content;

    private TaskEntity task;
    private ProjectEntity project;
    private ProjectMemberEntity projectMember;
    private CommentEntity comment;


    @Mock
    CommentRepository commentRepository;

    @Mock
    TaskRepository taskRepository;

    @Mock
    ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        taskId = 10L;
        projectId = 1L;
        projectMemberId = 8L;
        commentId = 5L;
        content = "test";

        project = new ProjectEntity(
                projectId,
                "projectName",
                ProjectState.ACTIVE,
                LocalDateTime.now(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );


        task = new TaskEntity(
                "taskName",
                project,
                null,
                content
        );


        projectMember = new ProjectMemberEntity(
                projectMemberId,
                project,
                1L,
                ProjectMemberRole.ADMIN,
                false,
                new ArrayList<>(),
                new ArrayList<>()
        );

        comment = new CommentEntity(
                1L,
                task,
                projectMember,
                content,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

    }


    @Test
    void createComment() {
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(projectMemberRepository.findById(projectMemberId)).willReturn(Optional.of(projectMember));
        given(commentRepository.save(any(CommentEntity.class))).willReturn(comment);

        CommentResponse response =  commentService.createComment(taskId, projectMemberId, content);

        then(taskRepository).should().findById(taskId);
        then(projectMemberRepository).should().findById(projectMemberId);
        then(commentRepository).should().save(any(CommentEntity.class));

        assertEquals(content, response.content());
    }

    @Test
    void getComments() {
        given(taskRepository.existsByProject_IdAndId(projectId, taskId))
                .willReturn(true);

        given(commentRepository.findAllByTask_Id(taskId))
                .willReturn(List.of(comment));

        List<CommentResponse> responses = commentService.getComments(projectId, taskId);

        then(taskRepository).should().existsByProject_IdAndId(projectId, taskId);
        then(commentRepository).should().findAllByTask_Id(taskId);

        assertEquals(1, responses.getFirst().id());
        assertEquals(content, responses.getFirst().content());
    }

    @Test
    void updateComment() {
        String updateContent = "updateContent";

        given(projectMemberRepository.findByProject_IdAndUserId(projectMemberId, projectId))
                .willReturn(Optional.of(projectMember));

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));

        given(commentRepository.findByIdAndTask_Id(commentId, taskId))
                .willReturn(Optional.of(comment));

        CommentResponse response = commentService.updateComment(projectId, taskId, commentId, projectMemberId, updateContent);

        then(projectMemberRepository).should().findByProject_IdAndUserId(projectMemberId, projectId);
        then(taskRepository).should().findById(taskId);
        then(commentRepository).should().findByIdAndTask_Id(commentId, taskId);

        assertEquals(updateContent, response.content());
    }

    @Test
    void deleteComment() {
        given(commentRepository.findByIdAndTask_Id(commentId, taskId))
                .willReturn(Optional.of(comment));

        commentService.deleteComment(projectId, taskId, commentId, projectMemberId);

        then(commentRepository).should().findByIdAndTask_Id(commentId, taskId);
        then(commentRepository).should().delete(comment);
    }
}
