package com.nhnacademy.task.tag;

import com.nhnacademy.task.project.ProjectEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * tag
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Entity
@Table(name = "task_tag")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
