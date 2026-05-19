package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.resp.TagResponse;

import java.util.List;

/**
 * TaskTagService
 *
 * @author chosun-nhn12
 * @since 26. 5. 18.
 */
public interface TaskTagService {
    TagResponse attachTag(Long projectId, Long taskId, Long tagId);

    List<TagResponse> getTaskTags(Long projectId, Long taskId);

    void detachTag(Long projectId, Long taskId, Long tagId);
}
