package com.nhnacademy.task.repository;

import com.nhnacademy.task.entity.TaskTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * TaskTagRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface TaskTagRepository extends JpaRepository<TaskTagEntity, Long> {
    List<TaskTagEntity> findAllByTask_Id(Long taskId);

    List<TaskTagEntity> findAllByTask_IdAndTask_Project_Id(Long taskId, Long projectId);

    Optional<TaskTagEntity> findByTag_IdAndTask_Id(Long tagId, Long taskId);

    Optional<TaskTagEntity> findByTag_IdAndTask_IdAndTask_Project_Id(Long tagId, Long taskId, Long projectId);

    boolean existsByTag_IdAndTask_Id(Long tagId, Long taskId);

    void deleteByTag_IdAndTask_Id(Long tagId, Long taskId);
}
