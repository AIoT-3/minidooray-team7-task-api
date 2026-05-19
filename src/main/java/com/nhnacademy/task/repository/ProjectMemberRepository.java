package com.nhnacademy.task.repository;

import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.ProjectMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, Long> {
    Optional<ProjectMemberEntity> findByProject_IdAndUserIdAndRole(Long projectId, Long userId, ProjectMemberRole role);

    Optional<ProjectMemberEntity> findByProject_IdAndUserId(Long projectEntityId, Long userId);

    List<ProjectMemberEntity> findAllByUserId(Long userId);

    boolean existsByUserIdAndProject_Id(Long requestingUserId, Long projectId);
    boolean existsByProject_IdAndUserIdAndRole(Long projectEntityId, Long userId, ProjectMemberRole role);

    List<ProjectMemberEntity> findAllByProject_Id(Long projectId);
}