package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.req.ProjectMemberCreateRequest;
import com.nhnacademy.task.dto.resp.ProjectMemberResponse;

import java.util.List;

public interface ProjectMemberService {
    //create
    void createProjectMember(Long requestingUserId, Long projectId, ProjectMemberCreateRequest projectMemberCreateRequest);

    //read
    ProjectMemberResponse getById(Long requestingUserId, Long projectMemberId);
    ProjectMemberResponse getByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId);
    List<ProjectMemberResponse> getAllByProjectId(Long requestingUserId, Long projectId);

    //update
    //delete
    void deleteProjectMemberByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId);
    void deleteProjectMemberByProjectIdAndProjectMemberId(Long requestingUserId, Long projectId, Long projectMemberId);
}
