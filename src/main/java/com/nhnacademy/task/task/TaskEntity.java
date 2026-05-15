package com.nhnacademy.task.task;

import com.nhnacademy.task.milestone.MilestoneEntity;
import com.nhnacademy.task.project.ProjectEntity;
import com.nhnacademy.task.project_member.ProjectMemberEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * task
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(max = 200)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Length(max = 200)
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_member_id")
    private ProjectMemberEntity projectMemberEntity;

    @ManyToOne(optional = true)
    @JoinColumn(name = "milestone_id")
    private MilestoneEntity milestoneEntity;
}
