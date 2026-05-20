package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.common.validator.BusinessRuleValidator;
import com.nhnacademy.task.dto.resp.*;
import com.nhnacademy.task.entity.*;
import com.nhnacademy.task.dto.req.ProjectCreateRequest;
import com.nhnacademy.task.dto.req.ProjectUpdateRequest;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.service.ProjectService;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private final BusinessRuleValidator businessRuleValidator;

    //re-checked
    @Transactional
    @Override
    public void createProject(Long requestingUserId, ProjectCreateRequest projectCreateRequest){
        //프로젝트 생성
        ProjectEntity projectEntity = new ProjectEntity(projectCreateRequest.name(), ProjectState.ACTIVE);
        ProjectEntity savedProject = projectRepository.save(projectEntity);

        //프로젝트 생성 요청자를 프로젝트 관리자로 즉시 등록
        ProjectMemberEntity projectAdmin = new ProjectMemberEntity(savedProject, requestingUserId, ProjectMemberRole.ADMIN);
        savedProject.addProjectMember(projectAdmin);
        projectMemberRepository.save(projectAdmin);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public ProjectSimpleResponse getProjectSimpleInfoById(Long requestingUserId, Long projectId){
        //프로젝트 존재 여부 검증 후 find
        ProjectEntity projectEntity = businessRuleValidator.findProjectOrThrow(projectId);
        //요청자가 프로젝트 멤버 인지 확인
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);

        return ProjectSimpleResponse.from(projectEntity);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public List<ProjectSimpleResponse> getProjectsByUserId(Long requestingUserId){
        List<ProjectSimpleResponse> allByUserId = projectRepository.findAllByUserId(requestingUserId);

        if(!allByUserId.isEmpty()){
            return allByUserId;
        }
        return new ArrayList<>();
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public ProjectDetailResponse getProjectDetailInfoById(Long requestingUserId, Long projectId) {
        //프로젝트 존재 여부 검증 후 find
        ProjectEntity projectEntity = businessRuleValidator.findProjectOrThrow(projectId);
        //요청자가 프로젝트 멤버 인지 확인
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);

        return ProjectDetailResponse.from(projectEntity);
    }

    //re-checked
    @Transactional
    @Override
    public void updateProject(Long requestingUserId, Long projectId, ProjectUpdateRequest projectUpdateRequest){
        //프로젝트 존재 여부 검증 후 find
        ProjectEntity projectEntity = businessRuleValidator.findProjectOrThrow(projectId);
        //요청자의 프로젝트 관리자 권한 확인
        businessRuleValidator.validateProjectAdmin(requestingUserId, projectId);

        String status = projectUpdateRequest.state();
        ProjectState projectState = ProjectState.valueOf(status);
        projectEntity.updateNameAndState(projectUpdateRequest.name(), projectState);

        projectRepository.save(projectEntity);
    }

    //re-checked
    @Transactional
    @Override
    public void deleteProject(Long requestingUserId, Long projectId){
        //프로젝트 존재 여부 확인
        businessRuleValidator.validateProjectExists(projectId);
        //요청 자가 해당 프로젝트의 관리자 인지 확인
        businessRuleValidator.validateProjectAdmin(requestingUserId,projectId);

        projectRepository.deleteById(projectId);
    }
}
