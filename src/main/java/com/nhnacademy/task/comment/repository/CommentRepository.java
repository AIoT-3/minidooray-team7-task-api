package com.nhnacademy.task.comment.repository;

import com.nhnacademy.task.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CommentRepository
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
