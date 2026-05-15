package com.nhnacademy.task.project.repository;

import com.nhnacademy.task.project.ProjectEntity;
import com.nhnacademy.task.project.dto.ProjectSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProjectRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    @Query("""
        SELECT NEW com.nhnacademy.task.project.dto.ProjectSimpleResponseDto(
            p.id, p.name, p.state, p.createdAt
        )
        FROM projects p
        INNER JOIN ProjectMemberEntity pm ON p.id = pm.projectEntity.id
        WHERE pm.userId = :userId
        ORDER BY p.createdAt DESC
    """)
    Page<ProjectSimpleResponseDto> findAllByUserId(
            @Param("userId") Long requestingUserId,
            Pageable pageable
    );
}
