package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.ProjectMemberRole;
import com.nhnacademy.task.dto.req.ProjectMemberCreateRequest;
import com.nhnacademy.task.dto.resp.ProjectMemberResponse;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void createProjectMember(Long requestingUserId, Long projectId, ProjectMemberCreateRequest projectMemberCreateRequest) {
        Optional<ProjectEntity> byId = projectRepository.findById(projectId);
        if(!byId.isEmpty()){
            throw new EntityNotFoundException(
                    String.format("id: project not found", projectId)
            );
        }

        if(!projectMemberRepository.existsByProjectEntity_IdAndUserIdAndRole(projectId, requestingUserId, ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        ProjectEntity projectEntity = byId.get();
        ProjectMemberEntity projectMemberEntity = new ProjectMemberEntity(projectMemberCreateRequest.userId(), ProjectMemberRole.USER, projectEntity);
        projectMemberRepository.save(projectMemberEntity);
    }

    @Override
    public ProjectMemberResponse getById(Long requestingUserId, Long projectMemberId) {
        return null;
    }

    @Override
    public ProjectMemberResponse getByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId) {
        return null;
    }

    @Override
    public List<ProjectMemberResponse> getAllByProjectId(Long requestingUserId, Long projectId) {
        return List.of();
    }

    @Override
    public void deleteProjectMember(Long requestingUserId, Long userId, Long projectId) {

    }
}
