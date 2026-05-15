package com.nhnacademy.task.tag.service;

import com.nhnacademy.task.tag.dto.TagResponseDto;
import com.nhnacademy.task.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<TagResponseDto> getTagsByProjectId(Long projectId) {
        return tagRepository.findAllByProjectEntity_Id(projectId);
    }
}
