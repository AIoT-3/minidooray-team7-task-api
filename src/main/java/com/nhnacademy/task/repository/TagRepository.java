package com.nhnacademy.task.repository;

import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * TagRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagResponse> findAllByProject_Id(Long projectId);
    Optional<TagEntity> findByIdAndProject_Id(Long tagId, Long projectId);
    boolean existsByProject_idAndName(Long projectId, String tagName);

    boolean existsByProject_idAndId(Long projectId, Long id);
}
