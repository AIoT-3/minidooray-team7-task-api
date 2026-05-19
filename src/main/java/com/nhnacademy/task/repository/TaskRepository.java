package com.nhnacademy.task.repository;

import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByProjectMember_IdAndProject_Id(Long projectMemberId, Long projectId);

    List<TaskEntity> findAllByProject_Id(Long projectId);
}
