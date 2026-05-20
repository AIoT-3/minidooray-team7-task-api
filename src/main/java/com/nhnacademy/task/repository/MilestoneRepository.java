package com.nhnacademy.task.repository;

import com.nhnacademy.task.entity.MilestoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<MilestoneEntity, Long> {
    List<MilestoneEntity> findAllByProject_Id(Long projectId);

    boolean existsByProject_IdAndId(Long projectId, Long id);
}
