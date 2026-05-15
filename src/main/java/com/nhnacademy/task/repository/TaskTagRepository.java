package com.nhnacademy.task.repository;

import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.entity.TaskTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * TaskTagRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface TaskTagRepository extends JpaRepository<TaskTagEntity, Long> {
    Optional<TagEntity> findByTagEntity_IdAndTaskEntity_Id(Long tagId, Long taskId);
}
