package com.nhnacademy.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectMember Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "project_members")
public class ProjectMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-projectMember")
    private ProjectEntity project;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "role", length = 10)
    @Enumerated(EnumType.STRING)
    private ProjectMemberRole role;

    @NotNull
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "projectMember", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference(value = "projectMember-task")
    private List<TaskEntity> taskList = new ArrayList<>();

    @OneToMany(mappedBy = "projectMember", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference(value = "projectMember-comment")
    private List<CommentEntity> commentList = new ArrayList<>();

    public ProjectMemberEntity(ProjectEntity project, Long userId, ProjectMemberRole role) {
        this.project = project;
        this.userId = userId;
        this.role = role;
        this.isDeleted = false;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    public void setProject(ProjectEntity project) {
        if (this.project != null) {
            this.project.getProjectMemberList().remove(this);
        }
        this.project = project;
        if (project != null && !project.getProjectMemberList().contains(this)) {
            project.getProjectMemberList().add(this);
        }
    }

    public void addTask(TaskEntity task) {
        task.setProjectMember(this);
    }

    public void removeTask(TaskEntity task) {
        task.setProjectMember(null);
    }



    public void addComment(CommentEntity comment) {
        comment.setProjectMember(this);
    }

    public void removeComment(CommentEntity comment) {
        comment.setProjectMember(null);
    }
}
