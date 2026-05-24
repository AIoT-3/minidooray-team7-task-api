package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.resp.TagResponse;

import java.util.List;

/**
 * TagService
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
public interface TagService {
    TagResponse createTag(Long projectId, String name);

    List<TagResponse> getTagsByProjectId(Long projectId);

    TagResponse updateTag(Long projectId, Long tagId, String name);

    void deleteTag(Long projectId, Long tagId);
}
