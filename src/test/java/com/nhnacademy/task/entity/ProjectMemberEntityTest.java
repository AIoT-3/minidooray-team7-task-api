package com.nhnacademy.task.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ProjectMemberEntityTest {

    @Mock
    private ProjectEntity mockProject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockProject.getProjectMemberList()).thenReturn(new ArrayList<>());
    }

    @Test
    void testProjectMemberEntityCreation() {
        Long userId = 1L;
        ProjectMemberRole role = ProjectMemberRole.ADMIN;
        ProjectMemberEntity member = new ProjectMemberEntity(mockProject, userId, role);

        assertThat(member.getProject()).isEqualTo(mockProject);
        assertThat(member.getUserId()).isEqualTo(userId);
        assertThat(member.getRole()).isEqualTo(role);
        assertThat(member.getIsDeleted()).isFalse();
    }

    @Test
    void testMarkAsDeleted() {
        ProjectMemberEntity member = new ProjectMemberEntity(mockProject, 1L, ProjectMemberRole.MEMBER);
        member.markAsDeleted();
        assertThat(member.getIsDeleted()).isTrue();
    }

    @Test
    void testRestore() {
        ProjectMemberEntity member = new ProjectMemberEntity(mockProject, 1L, ProjectMemberRole.MEMBER);
        member.markAsDeleted();
        member.restore();
        assertThat(member.getIsDeleted()).isFalse();
    }

    @Test
    void testSetProject() {
        ProjectMemberEntity member = new ProjectMemberEntity(null, 1L, ProjectMemberRole.MEMBER);
        member.setProject(mockProject);

        assertThat(member.getProject()).isEqualTo(mockProject);
        assertThat(mockProject.getProjectMemberList()).contains(member);

        member.setProject(null);
        assertThat(member.getProject()).isNull();
    }
}
