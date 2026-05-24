package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.validator.BusinessRuleValidator;
import com.nhnacademy.task.entity.ProjectEntity;
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

    private final BusinessRuleValidator businessRuleValidator;

    //re-checked
    @Transactional
    @Override
    public void createProjectMember(Long requestingUserId, Long projectId, ProjectMemberCreateRequest projectMemberCreateRequest) {
        // 프로젝트 존재 여부 검증 후 find
        ProjectEntity projectEntity = businessRuleValidator.findProjectOrThrow(projectId);
        // 요청자가 프로젝트의 관리자 인지 체크
        businessRuleValidator.validateProjectAdmin(requestingUserId, projectId);
        // 존재하는 유저인지 account-api를 통해 검증
        businessRuleValidator.findUserOrExists(projectMemberCreateRequest.userId());

        //이미 프로젝트 멤버로 등록되어 있는 유저 인지 확인 하기 위해 find
        Optional<ProjectMemberEntity> byProjectIdAndUserId = projectMemberRepository.findByProject_IdAndUserId(projectId, projectMemberCreateRequest.userId());
        if(byProjectIdAndUserId.isEmpty()) { // 프로젝트 멤버로 등록되어 있지 않은 경우
            ProjectMemberEntity projectMemberEntity = new ProjectMemberEntity(projectEntity, projectMemberCreateRequest.userId(), ProjectMemberRole.MEMBER);
            projectEntity.addProjectMember(projectMemberEntity);
            projectMemberRepository.save(projectMemberEntity);
        }
        else{ // 이미 프로젝트 멤버로 등록 되어있는 경우
            ProjectMemberEntity projectMemberEntity = byProjectIdAndUserId.get();
            projectMemberEntity.restore();
            projectMemberRepository.save(projectMemberEntity);
        }
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public ProjectMemberResponse getById(Long requestingUserId, Long projectMemberId) {
        // 프로젝트 멤버 존재 여부 검증 후 find
        ProjectMemberEntity projectMemberEntity = businessRuleValidator.findProjectMemberOrThrow(projectMemberId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectMemberEntity.getProject().getId());

        return ProjectMemberResponse.from(projectMemberEntity);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public ProjectMemberResponse getByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId) {
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 프로젝트 멤버 존재 여부 검증 후 find
        ProjectMemberEntity projectMemberEntity = businessRuleValidator.findProjectMemberOrThrow(projectId, userId);

        return ProjectMemberResponse.from(projectMemberEntity);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public List<ProjectMemberResponse> getAllByProjectId(Long requestingUserId, Long projectId) {
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);

        List<ProjectMemberEntity> allByProjectId = projectMemberRepository.findAllByProject_Id(projectId);
        return allByProjectId.stream().map(
                ProjectMemberResponse::from).toList();
    }

    //re-checked
    @Transactional
    @Override
    public void deleteProjectMemberByUserIdAndProjectId(Long requestingUserId, Long userId, Long projectId) {
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트의 관리자 인지 체크
        businessRuleValidator.validateProjectAdmin(requestingUserId, projectId);
        // 삭제하려는 유저가 프로젝트의 멤버 인지 검증 후 find
        ProjectMemberEntity projectMemberEntity
                = businessRuleValidator.findProjectMemberOrThrow(projectId, userId);
        // 관리자는 스스로를 프로젝트에서 추방 할 수 없음
        businessRuleValidator.validateMemberIsNotAdmin(projectId, projectMemberEntity.getId());

        projectMemberEntity.markAsDeleted();
    }

    //re-checked
    @Transactional
    @Override
    public void deleteProjectMemberByProjectIdAndProjectMemberId(Long requestingUserId, Long projectId, Long projectMemberId) {
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트의 관리자 인지 체크
        businessRuleValidator.validateProjectAdmin(requestingUserId, projectId);
        // 프로젝트 멤버 존재 여부 검중 후 find
        ProjectMemberEntity projectMemberEntity
                = businessRuleValidator.findProjectMemberOrThrow(projectMemberId);
        // 삭제하려는 프로젝트 멤버가 해당 프로젝트에 소속되어 있는지 검증
        businessRuleValidator.validateMemberBelongsToProject(projectId, projectMemberId);
        // 관리자는 스스로를 프로젝트에서 추방 할 수 없음
        businessRuleValidator.validateMemberIsNotAdmin(projectId, projectMemberId);

        projectMemberEntity.markAsDeleted();
    }
}
