package com.nhnacademy.task.service;

import com.nhnacademy.task.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TagService
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

//    public List<TagResponse> getTagsByProjectId(Long projectId) {
//        return tagRepository.findAllByProjectEntity_Id(projectId);
//    }
}
