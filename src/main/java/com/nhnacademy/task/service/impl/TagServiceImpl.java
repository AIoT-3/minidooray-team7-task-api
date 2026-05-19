package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityAlreadyExistsException;
import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.repository.TagRepository;
import com.nhnacademy.task.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ProjectRepository projectRepository;

    @Transactional
    @Override
    public TagResponse createTag(Long projectId, String name) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        if (tagRepository.existsByProject_IdAndName(projectId, name)) {
            throw new EntityAlreadyExistsException("tag already exists for project");
        }

        TagEntity tag = new TagEntity(project, name);
        TagEntity saved = tagRepository.save(tag);

        return toResponse(saved);
    }

    @Override
    public List<TagResponse> getTagsByProjectId(Long projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        List<TagEntity> tags = tagRepository.findAllByProject_IdOrderByNameAsc(projectId);
        List<TagResponse> tagResponses = new ArrayList<>();

        for (TagEntity tag : tags) {
            tagResponses.add(toResponse(tag));
        }

        return tagResponses;
    }

    @Transactional
    @Override
    public TagResponse updateTag(Long projectId, Long tagId, String name) {
        TagEntity tag = getTagInProject(projectId, tagId);

        if (!tag.getName().equals(name) && tagRepository.existsByProject_IdAndName(projectId, name)) {
            throw new EntityAlreadyExistsException("tag already exists for project");
        }

        tag.updateName(name);
        return toResponse(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long projectId, Long tagId) {
        TagEntity tag = getTagInProject(projectId, tagId);
        tagRepository.delete(tag);
    }

    private TagEntity getTagInProject(Long projectId, Long tagId) {
        return tagRepository.findByIdAndProject_Id(tagId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));
    }

    private TagResponse toResponse(TagEntity tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
