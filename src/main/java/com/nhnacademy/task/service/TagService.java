package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.TagEntity;

import java.util.List;

/**
 * TagService
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
public interface TagService {
    TagEntity createTag(ProjectEntity project, String name);
    List<TagResponse> getTagsByProjectId(Long projectId);
}
