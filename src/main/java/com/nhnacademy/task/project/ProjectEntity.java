package com.nhnacademy.task.project;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * Project
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "projects")
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
    private ProjectState state;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
