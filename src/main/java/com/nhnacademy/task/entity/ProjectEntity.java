package com.nhnacademy.task.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(max = 45)
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ProjectState state;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "project-projectMember")
    private List<ProjectMemberEntity> projectMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "project-milestone")
    private List<MilestoneEntity> milestoneList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "project-task")
    private List<TaskEntity> taskList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "project-tag")
    private List<TagEntity> tagList = new ArrayList<>();

    public ProjectEntity(String name, ProjectState state) {
        this.name = name;
        this.state = state;
        this.createdAt = LocalDateTime.now();
    }

    public ProjectEntity(String name, ProjectState state, LocalDateTime createdAt) {
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
    }

    public void addProjectMember(ProjectMemberEntity projectMember) {
        projectMember.setProject(this);
    }

    public void removeProjectMember(ProjectMemberEntity projectMember) {
        projectMember.setProject(null);
    }

    public void addMilestone(MilestoneEntity milestone) {
        milestone.setProject(this);
    }

    public void removeMilestone(MilestoneEntity milestone) {
        milestone.setProject(null);
    }

    public void addTask(TaskEntity task) {
        task.setProject(this);
    }

    public void removeTask(TaskEntity task) {
        task.setProject(null);
    }

    public void addTag(TagEntity tag) {
        tag.setProject(this);
    }

    public void removeTag(TagEntity tag) {
        tag.setProject(null);
    }

    public void updateNameAndState(String name, ProjectState state) {
        this.name = name;
        this.state = state;
    }
}