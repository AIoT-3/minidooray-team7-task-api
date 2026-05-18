package com.nhnacademy.task.tag;

import com.nhnacademy.task.project.ProjectEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "taskEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<TaskTagEntity> tasktags = new HashSet<>();
}
