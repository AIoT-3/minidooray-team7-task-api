package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.entity.CommentEntity;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.repository.CommentRepository;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment() {
        Long projectId = 1L;
        Long taskId = 1L;
        Long projectMemberId = 1L;
        String content = "New Comment";

        TaskEntity task = mock(TaskEntity.class);
        ProjectMemberEntity member = mock(ProjectMemberEntity.class);
        CommentEntity savedComment = mock(CommentEntity.class);
        LocalDateTime now = LocalDateTime.now();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectMemberRepository.findByProject_IdAndUserId(projectId, projectMemberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(savedComment);

        when(savedComment.getId()).thenReturn(10L);
        when(savedComment.getProjectMember()).thenReturn(member);
        when(member.getId()).thenReturn(projectMemberId);
        when(savedComment.getContent()).thenReturn(content);
        when(savedComment.getCreatedAt()).thenReturn(now);
        when(savedComment.getUpdatedAt()).thenReturn(now);

        CommentResponse response = commentService.createComment(projectId, taskId, projectMemberId, content);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.content()).isEqualTo(content);

        verify(task, times(1)).addComment(any(CommentEntity.class));
        verify(member, times(1)).addComment(any(CommentEntity.class));
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void testGetComments() {
        Long projectId = 1L;
        Long taskId = 1L;
        CommentEntity comment1 = mock(CommentEntity.class);
        CommentEntity comment2 = mock(CommentEntity.class);
        ProjectMemberEntity member = mock(ProjectMemberEntity.class);

        when(taskRepository.existsByProject_IdAndId(projectId, taskId)).thenReturn(true);
        when(commentRepository.findAllByTask_Id(taskId)).thenReturn(List.of(comment1, comment2));
        when(comment1.getProjectMember()).thenReturn(member);
        when(comment2.getProjectMember()).thenReturn(member);

        List<CommentResponse> responses = commentService.getComments(projectId, taskId);

        assertThat(responses).hasSize(2);
    }

    @Test
    void testUpdateComment() {
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;
        Long projectMemberId = 1L;
        String newContent = "Updated Content";
        
        ProjectMemberEntity member = mock(ProjectMemberEntity.class);
        TaskEntity task = mock(TaskEntity.class);
        CommentEntity comment = mock(CommentEntity.class);

        when(projectMemberRepository.findByProject_IdAndUserId(projectMemberId, projectId)).thenReturn(Optional.of(member));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findByIdAndTask_Id(commentId, taskId)).thenReturn(Optional.of(comment));
        
        when(comment.getProjectMember()).thenReturn(member);
        when(member.getId()).thenReturn(projectMemberId);
        
        when(comment.getId()).thenReturn(commentId);
        when(comment.getContent()).thenReturn(newContent); 

        CommentResponse response = commentService.updateComment(projectId, taskId, commentId, projectMemberId, newContent);

        assertThat(response.content()).isEqualTo(newContent);
        verify(comment, times(1)).updateContent(newContent);
    }
    
    @Test
    void testUpdateComment_NoPermission() {
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;
        Long requestingUserId = 1L;
        Long ownerId = 2L;
        String newContent = "Updated Content";
        
        ProjectMemberEntity requestingMember = mock(ProjectMemberEntity.class);
        ProjectMemberEntity ownerMember = mock(ProjectMemberEntity.class);
        TaskEntity task = mock(TaskEntity.class);
        CommentEntity comment = mock(CommentEntity.class);

        when(projectMemberRepository.findByProject_IdAndUserId(requestingUserId, projectId)).thenReturn(Optional.of(requestingMember));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findByIdAndTask_Id(commentId, taskId)).thenReturn(Optional.of(comment));
        
        when(comment.getProjectMember()).thenReturn(ownerMember);
        when(ownerMember.getId()).thenReturn(ownerId);

        assertThatThrownBy(() -> commentService.updateComment(projectId, taskId, commentId, requestingUserId, newContent))
                .isInstanceOf(NoPermissionException.class);
    }

    @Test
    void testDeleteComment() {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;

        ProjectEntity project = mock(ProjectEntity.class);
        TaskEntity task = mock(TaskEntity.class);
        ProjectMemberEntity requestingMember = mock(ProjectMemberEntity.class);
        ProjectMemberEntity ownerMember = mock(ProjectMemberEntity.class);
        CommentEntity comment = mock(CommentEntity.class);

        when(comment.getTask()).thenReturn(task);
        when(comment.getProjectMember()).thenReturn(ownerMember);
        when(task.getProject()).thenReturn(project);

        when(project.getId()).thenReturn(projectId);

        when(projectMemberRepository.findByProject_IdAndUserId(projectId, requestingUserId)).thenReturn(Optional.of(requestingMember));

        Long memberPrimaryKeyId = 100L;
        when(requestingMember.getId()).thenReturn(memberPrimaryKeyId);
        when(ownerMember.getId()).thenReturn(memberPrimaryKeyId);

        when(commentRepository.findByIdAndTask_Id(commentId, taskId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(projectId, taskId, commentId, requestingUserId);

        verify(task, times(1)).removeComment(comment);
        verify(ownerMember, times(1)).removeComment(comment);
        verify(commentRepository, times(1)).delete(comment);
    }
}
