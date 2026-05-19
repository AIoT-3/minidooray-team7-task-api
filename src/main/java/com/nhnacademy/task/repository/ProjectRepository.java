package com.nhnacademy.task.repository;

import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.dto.resp.ProjectSimpleResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    @Query("""
    SELECT NEW com.nhnacademy.task.dto.resp.ProjectSimpleResponse(
        p.id, p.name, p.state, p.createdAt
    )
    FROM ProjectEntity p
    WHERE p.id IN (
        SELECT pm.project.id
        FROM ProjectMemberEntity pm
        WHERE pm.userId = :userId
    )
    ORDER BY p.createdAt DESC
""")
    List<ProjectSimpleResponse> findAllByUserId(
            @Param("userId") Long userId
    );
}