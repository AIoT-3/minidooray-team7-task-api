package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.repository.TagRepository;
import com.nhnacademy.task.service.TagService;
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
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public TagEntity createTag(ProjectEntity project, String name) {
        TagEntity tag = new TagEntity(project, name);
        project.addTag(tag);
        
        return tag;
    }

    public List<TagResponse> getTagsByProjectId(Long projectId) {
        return tagRepository.findAllByProject_Id(projectId);
    }
}
