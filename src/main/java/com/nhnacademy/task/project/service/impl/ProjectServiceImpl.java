package com.nhnacademy.task.project.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.project.ProjectEntity;
import com.nhnacademy.task.project.ProjectState;
import com.nhnacademy.task.project.dto.req.ProjectCreateRequest;
import com.nhnacademy.task.project.dto.resp.ProjectSimpleResponse;
import com.nhnacademy.task.project.dto.req.ProjectUpdateRequest;
import com.nhnacademy.task.project.repository.ProjectRepository;
import com.nhnacademy.task.project.service.ProjectService;
import com.nhnacademy.task.project_member.ProjectMemberEntity;
import com.nhnacademy.task.project_member.ProjectMemberRole;
import com.nhnacademy.task.project_member.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void createProject(Long requestingUserId, ProjectCreateRequest projectCreateRequest){
        //프로젝트 생성
        ProjectEntity projectEntity = new ProjectEntity(projectCreateRequest.name(), ProjectState.ACTIVE);
        ProjectEntity savedProject = projectRepository.save(projectEntity);

        //프로젝트 생성 요청자를 프로젝트 관리자로 즉시 등록
        ProjectMemberEntity projectAdmin = new ProjectMemberEntity(requestingUserId, ProjectMemberRole.ADMIN, savedProject);
        projectMemberRepository.save(projectAdmin);
    }

    @Transactional(readOnly = true)
    public ProjectSimpleResponse getProjectById(Long requestingUserId, Long projectId){
        //프로젝트 find
        Optional<ProjectEntity> projectById = projectRepository.findById(projectId);
        if(projectById.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("id: project not found", projectId)
            );
        }
        ProjectEntity projectEntity = projectById.get();

        //요청자의 프로젝트 멤버 권한 확인
        Optional<ProjectMemberEntity> byProjectEntityIdAndUserId = projectMemberRepository.findByProjectEntity_IdAndUserId(projectId, requestingUserId);
        if(byProjectEntityIdAndUserId.isEmpty()){
            throw new NoPermissionException(
                    String.format("id:%s user has no member permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        return new ProjectSimpleResponse(projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getState().name(),
                projectEntity.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public Page<ProjectSimpleResponse> getProjectsPageByUserId(Long requestingUserId, Pageable pageable){
        Page<ProjectSimpleResponse> allByUserId = projectRepository.findAllByUserId(requestingUserId, pageable);

        if(!allByUserId.isEmpty()){
            return allByUserId;
        }
        return Page.empty();
    }

    @Transactional
    public void updateProject(Long requestingUserId, Long projectId, ProjectUpdateRequest projectUpdateRequest){
        //기존 프로젝트 find
        Optional<ProjectEntity> projectById = projectRepository.findById(projectId);
        if(projectById.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("id: project not found", projectId)
            );
        }
        ProjectEntity projectEntity = projectById.get();

        //요청자의 관리자 권한 확인
        Optional<ProjectMemberEntity> projectMemberById = projectMemberRepository.findByProjectEntity_IdAndUserIdAndRole(
                projectId, requestingUserId, ProjectMemberRole.ADMIN);
        if(projectMemberById.isEmpty()){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        //프로젝트 update
        String status = projectUpdateRequest.status();
        ProjectState projectState = ProjectState.valueOf(status);
        ProjectEntity updatedProject = new ProjectEntity(projectEntity.getId(),
                projectUpdateRequest.name(),
                projectState,
                projectEntity.getCreatedAt());

        //update 된 프로젝트 save
        projectRepository.save(updatedProject);
    }

    @Transactional
    public void deleteProject(Long requestingUserId, Long projectId){
        //프로젝트 존재 여부 확인
        if(!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException(
                    String.format("id: project not found", projectId)
            );
        }

        //요청 자가 해당 프로젝트의 관리자 인지 확인
        Optional<ProjectMemberEntity> projectMemberById = projectMemberRepository.findByProjectEntity_IdAndUserIdAndRole(
                projectId, requestingUserId, ProjectMemberRole.ADMIN);
        if(projectMemberById.isEmpty()){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        projectRepository.deleteById(projectId);
    }
}
