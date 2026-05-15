package com.nhnacademy.task.tag.repository;

import com.nhnacademy.task.tag.TaskTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TaskTagRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface TaskTagRepository extends JpaRepository<TaskTagEntity, Long> {
}
