package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.NoPermissionException;
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
        //프로젝트 find
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                )
        );


        //요청자의 프로젝트 멤버 권한 확인
        if(!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)){
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
        //프로젝트 find
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                )
        );

        //요청자의 프로젝트 멤버 권한 확인
        if(!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId,projectId)){
            throw new NoPermissionException(
                    String.format("id:%s user has no member permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        List<MileStoneSimpleResponse> milestoneDtos = projectEntity.getMilestoneList().stream()
                .map(milestoneEntity -> {
                    return new MileStoneSimpleResponse(milestoneEntity.getId(), milestoneEntity.getName());
                })
                .toList();

        List<TaskSimpleResponse> taskDtos = projectEntity.getTaskList().stream()
                .map(taskEntity -> {
                    long id = taskEntity.getId();
                    long projectMemberId = taskEntity.getProjectMember().getId();
                    String name = taskEntity.getName();
                    MileStoneSimpleResponse mileStone = new MileStoneSimpleResponse(taskEntity.getId(), taskEntity.getName());

                    List<TagResponse> tagList = new ArrayList<>();
                    for (TaskTagEntity taskTagEntity : taskEntity.getTaskTagList()) {
                        tagList.add(new TagResponse(taskTagEntity.getId(),
                                taskTagEntity.getTag().getName()));
                    }
                    return new TaskSimpleResponse(id, projectMemberId, name, mileStone, tagList);
                })
                .toList();

        List<ProjectMemberResponse> memberDtos = projectEntity.getProjectMemberList().stream()
                .map(projectMemberEntity -> {
                    return new ProjectMemberResponse(
                            projectMemberEntity.getId(),
                            projectMemberEntity.getUserId(),
                            projectMemberEntity.getRole().name(),
                            projectMemberEntity.getIsDeleted());
                })
                .toList();


        return new ProjectDetailResponse(projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getState().name(),
                projectEntity.getCreatedAt(),
                milestoneDtos,
                taskDtos,
                memberDtos);
    }

    //re-checked
    @Transactional
    @Override
    public void updateProject(Long requestingUserId, Long projectId, ProjectUpdateRequest projectUpdateRequest){
        //기존 프로젝트 find
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                )
        );

        //요청자의 관리자 권한 확인
        if(!projectMemberRepository.existsByProject_IdAndUserIdAndRole(projectId, requestingUserId, ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        //프로젝트 update
        String status = projectUpdateRequest.status();
        ProjectState projectState = ProjectState.valueOf(status);
        // 변경 감지로 인해 update
        projectEntity.updateNameAndState(projectUpdateRequest.name(), projectState);
        // 그러나 명시적으로 변경
        projectRepository.save(projectEntity);
    }

    //re-checked
    @Transactional
    @Override
    public void deleteProject(Long requestingUserId, Long projectId){
        //프로젝트 존재 여부 확인
        if(!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        //요청 자가 해당 프로젝트의 관리자 인지 확인
        if(!projectMemberRepository.existsByProject_IdAndUserIdAndRole(projectId, requestingUserId, ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        projectRepository.deleteById(projectId);
    }
}
