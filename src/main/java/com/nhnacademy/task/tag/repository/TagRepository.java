package com.nhnacademy.task.tag.repository;

import com.nhnacademy.task.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TagRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
