package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.req.ProjectCreateRequest;
import com.nhnacademy.task.dto.resp.ProjectSimpleResponse;
import com.nhnacademy.task.dto.req.ProjectUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ProjectService
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface ProjectService {
    //create
    void createProject(Long requestingUserId, ProjectCreateRequest projectCreateRequest);

    //read
    ProjectSimpleResponse getProjectSimpleInfoById(Long requestingUserId, Long projectId);
    // ProjectDetailResponse getProjectDetailInfoById(Long requestingUserId, Long projectId);
    Page<ProjectSimpleResponse> getProjectsPageByUserId(Long requestingUserId, Pageable pageable);

    //update
    void updateProject(Long requestingUserId, Long projectId, ProjectUpdateRequest projectUpdateRequest);

    //delete
    void deleteProject(Long requestingUserId, Long projectId);
}
