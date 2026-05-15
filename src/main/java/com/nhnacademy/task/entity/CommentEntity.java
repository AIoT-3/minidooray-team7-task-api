package com.nhnacademy.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Comment Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonBackReference(value = "task-comment")
    private TaskEntity task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_member_id")
    @JsonBackReference(value = "projectMember-comment")
    private ProjectMemberEntity projectMember;

    @NotBlank
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CommentEntity(TaskEntity task, ProjectMemberEntity projectMember, String content) {
        this.task = task;
        this.projectMember = projectMember;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void setTask(TaskEntity task) {
        if (this.task != null) {
            this.task.getCommentList().remove(this);
        }
        this.task = task;
        if (task != null && !task.getCommentList().contains(this)) {
            task.getCommentList().add(this);
        }
    }

    public void setProjectMember(ProjectMemberEntity projectMember) {
        if (this.projectMember != null) {
            this.projectMember.getCommentList().remove(this);
        }
        this.projectMember = projectMember;
        if (projectMember != null && !projectMember.getCommentList().contains(this)) {
            projectMember.getCommentList().add(this);
        }
    }
}