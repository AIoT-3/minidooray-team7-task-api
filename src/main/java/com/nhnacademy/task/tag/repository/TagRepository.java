package com.nhnacademy.task.tag.repository;

import com.nhnacademy.task.tag.TagEntity;
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
    List<TagEntity> findAllByProjectEntity_Id(Long projectId);
    Optional<TagEntity> findByIdAndProjectEntity_Id(Long tagId, Long projectId);
    boolean existsByProjectEntity_idAndName(Long projectId, String tagName);
}
