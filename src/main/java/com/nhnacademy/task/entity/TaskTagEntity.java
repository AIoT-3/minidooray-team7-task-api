package com.nhnacademy.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TaskTag Entity (Task - Tag 연결 테이블)
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "task_tag")
public class TaskTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    @JsonBackReference(value = "tag-taskTag")
    private TagEntity tag;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonBackReference(value = "task-taskTag")
    private TaskEntity task;

    public TaskTagEntity(TagEntity tag, TaskEntity task) {
        this.tag = tag;
        this.task = task;
    }

    public void setTag(TagEntity tag) {
        if (this.tag != null) {
            this.tag.getTaskTagList().remove(this);
        }
        this.tag = tag;
        if (tag != null && !tag.getTaskTagList().contains(this)) {
            tag.getTaskTagList().add(this);
        }
    }

    public void setTask(TaskEntity task) {
        if (this.task != null) {
            this.task.getTaskTagList().remove(this);
        }
        this.task = task;
        if (task != null && !task.getTaskTagList().contains(this)) {
            task.getTaskTagList().add(this);
        }
    }
}