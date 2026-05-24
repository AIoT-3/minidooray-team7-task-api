package com.nhnacademy.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * Tag Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-tag")
    private ProjectEntity project;

    @NotBlank
    @Length(max = 50)
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "tag-taskTag")
    private List<TaskTagEntity> taskTagList = new ArrayList<>();

    public TagEntity(ProjectEntity project, String name) {
        this.project = project;
        this.name = name;
        createdAt = LocalDateTime.now();
    }

    public TagEntity(ProjectEntity project, String name, LocalDateTime createdAt) {
        this.project = project;
        this.name = name;
        this.createdAt = createdAt;
    }

    public void setProject(ProjectEntity project) {
        if (this.project != null) {
            this.project.getTagList().remove(this);
        }
        this.project = project;
        if (project != null && !project.getTagList().contains(this)) {
            project.getTagList().add(this);
        }
    }

    public void addTaskTag(TaskTagEntity taskTag) {
        taskTag.setTag(this);
    }

    public TagEntity updateName(String name) {
        this.name = name;
        return this;
    }

    public void removeTaskTag(TaskTagEntity taskTag) {
        taskTag.setTag(null);
    }
}