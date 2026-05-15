package com.nhnacademy.task.project.repository;

import com.nhnacademy.task.project.ProjectEntity;
import com.nhnacademy.task.project.dto.resp.ProjectSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * ProjectRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    @Query("""
    SELECT NEW com.nhnacademy.task.project.dto.resp.ProjectSimpleResponse(
        p.id, p.name, p.state, p.createdAt
    )
    FROM projects p
    WHERE p.id IN (
        SELECT pm.projectEntity.id
        FROM project_members pm
        WHERE pm.userId = :userId
    )
    ORDER BY p.createdAt DESC
""")
    Page<ProjectSimpleResponse> findAllByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
