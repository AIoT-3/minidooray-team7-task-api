package com.nhnacademy.task.project_member;

import com.nhnacademy.task.project.ProjectEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * member
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "project_members")
public class ProjectMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "role")
    private ProjectMemberRole role;

    @ManyToOne(optional = false)
    private ProjectEntity projectEntity;
}
