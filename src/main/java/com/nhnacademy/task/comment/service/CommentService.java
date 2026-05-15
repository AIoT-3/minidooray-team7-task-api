package com.nhnacademy.task.comment.service;

import com.nhnacademy.task.comment.dto.CommentResponseDto;
import com.nhnacademy.task.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CommentService
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

//    public List<CommentResponseDto> getComments(Long taskEntityId) {
//        return commentRepository.findAllByTaskEntityId(taskEntityId);
//    }
}
