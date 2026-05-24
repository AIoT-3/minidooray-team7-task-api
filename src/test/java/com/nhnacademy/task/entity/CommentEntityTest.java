package com.nhnacademy.task.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CommentEntityTest {

    @Mock
    private TaskEntity mockTask;

    @Mock
    private ProjectMemberEntity mockProjectMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockTask.getCommentList()).thenReturn(new ArrayList<>());
        when(mockProjectMember.getCommentList()).thenReturn(new ArrayList<>());
    }

    @Test
    void testCommentEntityCreation() {
        String content = "Test Content";
        CommentEntity comment = new CommentEntity(mockTask, mockProjectMember, content);

        assertThat(comment.getTask()).isEqualTo(mockTask);
        assertThat(comment.getProjectMember()).isEqualTo(mockProjectMember);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getCreatedAt()).isNotNull();
        assertThat(comment.getUpdatedAt()).isEqualTo(comment.getCreatedAt());

        assertThat(mockTask.getCommentList()).contains(comment);
        assertThat(mockProjectMember.getCommentList()).contains(comment);
    }

    @Test
    void testUpdateContent() {
        CommentEntity comment = new CommentEntity(mockTask, mockProjectMember, "Old Content");
        String newContent = "New Content";

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        comment.updateContent(newContent);

        assertThat(comment.getContent()).isEqualTo(newContent);
        assertThat(comment.getUpdatedAt()).isAfter(comment.getCreatedAt());
    }

    @Test
    void testSetTask() {
        CommentEntity comment = new CommentEntity(null, mockProjectMember, "Content");

        comment.setTask(mockTask);

        assertThat(comment.getTask()).isEqualTo(mockTask);
        assertThat(mockTask.getCommentList()).contains(comment);

        comment.setTask(null);
        assertThat(comment.getTask()).isNull();
    }

    @Test
    void testSetProjectMember() {
        CommentEntity comment = new CommentEntity(mockTask, null, "Content");

        comment.setProjectMember(mockProjectMember);

        assertThat(comment.getProjectMember()).isEqualTo(mockProjectMember);
        assertThat(mockProjectMember.getCommentList()).contains(comment);

        comment.setProjectMember(null);
        assertThat(comment.getProjectMember()).isNull();
    }
}
