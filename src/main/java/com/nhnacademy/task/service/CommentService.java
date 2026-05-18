package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.repository.CommentRepository;
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

    public List<CommentResponse> getComments(Long taskEntityId) {
        return commentRepository.findAllByTaskEntity_Id(taskEntityId);
    }
}
