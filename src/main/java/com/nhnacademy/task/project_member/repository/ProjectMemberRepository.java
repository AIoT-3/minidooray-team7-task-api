package com.nhnacademy.task.project_member.repository;

import com.nhnacademy.task.project_member.ProjectMemberEntity;
import com.nhnacademy.task.project_member.ProjectMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * MemberRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, Long> {
    Optional<ProjectMemberEntity> findByProjectEntity_IdAndUserIdAndRole(Long projectEntityId, Long userId, ProjectMemberRole role);

    Optional<ProjectMemberEntity> findByProjectEntity_IdAndUserId(Long projectEntityId, Long userId);

    List<ProjectMemberEntity> findAllByUserId(Long userId);
}