package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.req.ProjectMemberCreateRequest;
import com.nhnacademy.task.dto.resp.ProjectMemberResponse;

import java.util.List;

/**
 * MemberService
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface ProjectMemberService {
    //create
    void createProjectMember(Long requestingUserId, Long projectId, ProjectMemberCreateRequest projectMemberCreateRequest);

    //read
    ProjectMemberResponse getById(Long requestingUserId, Long projectMemberId);
    ProjectMemberResponse getByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId);
    List<ProjectMemberResponse> getAllByProjectId(Long requestingUserId, Long projectId);

    //update
    //delete
    void deleteProjectMember(Long requestingUserId, Long userId, Long projectId);

}
