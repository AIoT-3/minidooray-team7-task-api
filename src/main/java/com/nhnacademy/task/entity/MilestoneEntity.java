package com.nhnacademy.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nhnacademy.task.dto.req.MileStoneCreateRequest;
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
 * Milestone Entity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "milestones")
public class MilestoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(max = 50)
    @Column(name = "name")
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-milestone")
    private ProjectEntity project;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "milestone", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "milestone-task")
    private List<TaskEntity> taskList = new ArrayList<>();

    public MilestoneEntity(String name, ProjectEntity project, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.project = project;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = LocalDateTime.now();
    }

    public void setProject(ProjectEntity project) {
        if (this.project != null) {
            this.project.getMilestoneList().remove(this);
        }
        this.project = project;
        if (project != null && !project.getMilestoneList().contains(this)) {
            project.getMilestoneList().add(this);
        }
    }

    public void addTask(TaskEntity task) {
        task.setMilestone(this);
    }

    public void removeTask(TaskEntity task) {
        task.setMilestone(null);
    }

    public void updateNameAndStartDateAndEndDate(String name, LocalDateTime startDate, LocalDateTime endDate){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static MilestoneEntity create(MileStoneCreateRequest createRequest,
                                         ProjectEntity projectEntity){
        return new MilestoneEntity(
                createRequest.name(),
                projectEntity,
                createRequest.startDate(),
                createRequest.endDate()
        );
    }
}