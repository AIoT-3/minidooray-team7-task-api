package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.InvalidRequestException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    //re-checked
    @Transactional
    @Override
    public void createProjectMember(Long requestingUserId, Long projectId, ProjectMemberCreateRequest projectMemberCreateRequest) {
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                )
        );


        if(!projectMemberRepository.existsByProject_IdAndUserIdAndRole(projectId, requestingUserId, ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        Optional<ProjectMemberEntity> byProjectIdAndUserId = projectMemberRepository.findByProject_IdAndUserId(projectId, projectMemberCreateRequest.userId());
        if(byProjectIdAndUserId.isEmpty()) {
            ProjectMemberEntity projectMemberEntity = new ProjectMemberEntity(projectEntity, projectMemberCreateRequest.userId(), ProjectMemberRole.USER);
            projectEntity.addProjectMember(projectMemberEntity);
            projectMemberRepository.save(projectMemberEntity);
        }
        else{
            ProjectMemberEntity projectMemberEntity = byProjectIdAndUserId.get();
            projectMemberEntity.restore();
            projectMemberRepository.save(projectMemberEntity);
        }
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public ProjectMemberResponse getById(Long requestingUserId, Long projectMemberId) {
        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findById(projectMemberId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project member not found", projectMemberId)
                )
        );

        if(!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectMemberEntity.getProject().getId())){
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectMemberEntity.getProject().getId())
            );
        }

        return new ProjectMemberResponse(
                projectMemberEntity.getId(),
                projectMemberEntity.getUserId(),
                projectMemberEntity.getRole().name(),
                projectMemberEntity.getIsDeleted());
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public ProjectMemberResponse getByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId) {
        if(!projectRepository.existsById(projectId)){
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if(!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)){
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProject_IdAndUserId(projectId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s user not found in id:%s project", userId, projectId)
                )
        );

        return new ProjectMemberResponse(
                projectMemberEntity.getId(),
                projectMemberEntity.getUserId(),
                projectMemberEntity.getRole().name(),
                projectMemberEntity.getIsDeleted());
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public List<ProjectMemberResponse> getAllByProjectId(Long requestingUserId, Long projectId) {
        if(!projectRepository.existsById(projectId)){
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if(!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)){
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        List<ProjectMemberEntity> allByProjectId = projectMemberRepository.findAllByProject_Id(projectId);

        List<ProjectMemberResponse> projectMemberResponses =
                allByProjectId.stream().map(projectMemberEntity -> {
                    return new ProjectMemberResponse(
                            projectMemberEntity.getId(),
                            projectMemberEntity.getUserId(),
                            projectMemberEntity.getRole().name(),
                            projectMemberEntity.getIsDeleted());
                }).toList();

        return projectMemberResponses;
    }

    //re-checked
    @Transactional
    @Override
    public void deleteProjectMemberByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId) {
        if(!projectRepository.existsById(projectId)){
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if(!projectMemberRepository.existsByProject_IdAndUserIdAndRole(projectId, requestingUserId, ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProject_IdAndUserId(projectId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s user not found in id:%s project", userId, projectId)
                )
        );

        if(projectMemberEntity.getRole().equals(ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s admin cannot delete themselves from id:%s project",
                            userId, projectId)
            );
        }

        projectMemberEntity.markAsDeleted();
    }

    //re-checked
    @Transactional
    @Override
    public void deleteProjectMemberByProjectIdAndProjectMemberId(Long requestingUserId, Long projectId, Long projectMemberId) {
        if(!projectRepository.existsById(projectId)){
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if(!projectMemberRepository.existsByProject_IdAndUserIdAndRole(projectId, requestingUserId, ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findById(projectMemberId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s projectMember not found", projectMemberId)
                ));

        if(!projectMemberEntity.getProject().getId().equals(projectId)){
            throw new InvalidRequestException(
                    String.format("id:%s projectMember does not belong to projectId:%s",
                            projectMemberId, projectId)
            );
        }

        if(projectMemberEntity.getRole().equals(ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s project member admin cannot delete themselves from id:%s project",
                            projectMemberId, projectId)
            );
        }

        projectMemberEntity.markAsDeleted();
    }
}
