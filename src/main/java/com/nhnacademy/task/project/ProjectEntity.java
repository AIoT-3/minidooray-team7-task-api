package com.nhnacademy.task.project;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Project
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Entity
public class ProjectEntity {
    @Id
    private String id;
}
