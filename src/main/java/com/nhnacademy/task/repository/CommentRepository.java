package com.nhnacademy.task.repository;

import com.nhnacademy.task.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * CommentRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByTaskEntityId(Long taskEntityId);

    Optional<CommentEntity> findByIdAndTaskEntityId(Long id, Long taskEntityId);
}
