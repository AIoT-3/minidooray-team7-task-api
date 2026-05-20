package com.nhnacademy.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nhnacademy.task.dto.req.TaskCreateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Task Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-task")
    private ProjectEntity project;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_members_id")
    @JsonBackReference(value = "projectMember-task")
    private ProjectMemberEntity projectMember;

    //null 허용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    @JsonBackReference(value = "milestone-task")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private MilestoneEntity milestone;

    @NotBlank
    @Length(max = 200)
    @Column(name = "name")
    private String name;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "task-comment")
    private List<CommentEntity> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "task-taskTag")
    private List<TaskTagEntity> taskTagList = new ArrayList<>();

    public TaskEntity(String name,
                      ProjectEntity project,
                      ProjectMemberEntity projectMember,
                      String content) {
        this.name = name;
        this.project = project;
        this.projectMember = projectMember;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public void setProject(ProjectEntity project) {
        if (this.project != null) {
            this.project.getTaskList().remove(this);
        }
        this.project = project;
        if (project != null && !project.getTaskList().contains(this)) {
            project.getTaskList().add(this);
        }
    }

    public void setProjectMember(ProjectMemberEntity projectMember) {
        if (this.projectMember != null) {
            this.projectMember.getTaskList().remove(this);
        }
        this.projectMember = projectMember;
        if (projectMember != null && !projectMember.getTaskList().contains(this)) {
            projectMember.getTaskList().add(this);
        }
    }

    public void setMilestone(MilestoneEntity milestone) {
        if (this.milestone != null) {
            this.milestone.getTaskList().remove(this);
        }
        this.milestone = milestone;
        if (milestone != null && !milestone.getTaskList().contains(this)) {
            milestone.getTaskList().add(this);
        }
    }

    public void addComment(CommentEntity comment) {
        comment.setTask(this);
    }

    public void removeComment(CommentEntity comment) {
        comment.setTask(null);
    }

    public void addTaskTag(TaskTagEntity taskTag) {
        taskTag.setTask(this);
    }

    public void removeTaskTag(TaskTagEntity taskTag) {
        taskTag.setTask(null);
    }

    public void updateNameAndContent(String name, String content){
        this.name = name;
        this.content = content;
    }

    public static TaskEntity create(TaskCreateRequest request, ProjectEntity project, ProjectMemberEntity member) {
        return new TaskEntity(
                request.name(),
                project,
                member,
                request.content()
        );
    }
}