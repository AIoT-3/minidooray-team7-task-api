package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.InvalidRequestException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.common.validator.BusinessRuleValidator;
import com.nhnacademy.task.dto.req.TaskCreateRequest;
import com.nhnacademy.task.dto.req.TaskMileStoneCreateRequest;
import com.nhnacademy.task.dto.req.TaskUpdateRequest;
import com.nhnacademy.task.dto.resp.*;
import com.nhnacademy.task.entity.MilestoneEntity;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.repository.MilestoneRepository;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.repository.TaskRepository;
import com.nhnacademy.task.service.MilestoneService;
import com.nhnacademy.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    private final BusinessRuleValidator businessRuleValidator;

    //re-checked
    @Transactional
    @Override
    public void createTask(Long requestingUserId, Long projectId, TaskCreateRequest taskCreateRequest){
        // 프로젝트 존재 여부 검증 후 find
        ProjectEntity projectEntity
                = businessRuleValidator.findProjectOrThrow(projectId);
        // 프로젝트 멤버 권한 검증
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 프로젝트 멤버 존재 여부 검증 후 find
        ProjectMemberEntity projectMemberEntity
                = businessRuleValidator.findProjectMemberOrThrow(projectId, requestingUserId);

        TaskEntity taskEntity = TaskEntity.create(
                taskCreateRequest,
                projectEntity,
                projectMemberEntity);

        projectEntity.addTask(taskEntity);
        projectMemberEntity.addTask(taskEntity);
        taskRepository.save(taskEntity);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public TaskSimpleResponse getTaskSimpleInfoById(Long requestingUserId, Long projectId, Long taskId) {
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 태스크 존재 여부 검증 후 find
        TaskEntity taskEntity = businessRuleValidator.findTaskOrThrow(taskId);
        // 태스크가 프로젝트에 속하는지 검증
        businessRuleValidator.validateTaskBelongsToProject(projectId, taskId);

        return TaskSimpleResponse.from(taskEntity);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public TaskDetailResponse getTaskDetailInfoById(Long requestingUserId, Long projectId, Long taskId){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 태스크 존재 여부 검증 후 find
        TaskEntity taskEntity = businessRuleValidator.findTaskOrThrow(taskId);
        // 태스크가 프로젝트에 속하는지 검증
        businessRuleValidator.validateTaskBelongsToProject(projectId, taskId);

        return TaskDetailResponse.from(taskEntity);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public List<TaskSimpleResponse> getTasksByProjectID(Long requestingUserId, Long projectId){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);

        List<TaskEntity> allByProjectId = taskRepository.findAllByProject_Id(projectId);

        return allByProjectId != null ?
                allByProjectId.stream().map(
                        TaskSimpleResponse::from
                ).toList() : new ArrayList<>();
    }

    //re-checked
    @Transactional
    @Override
    public void updateTask(Long requestingUserId, Long projectId, Long taskId, TaskUpdateRequest taskUpdateRequest){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 태스크 존재 여부 검증 후 find
        TaskEntity taskEntity = businessRuleValidator.findTaskOrThrow(taskId);
        // 태스크가 프로젝트에 속하는지 검증
        businessRuleValidator.validateTaskBelongsToProject(projectId, taskId);

        taskEntity.updateNameAndContent(taskUpdateRequest.name(), taskUpdateRequest.content());
        taskRepository.save(taskEntity);
    }

    @Transactional
    @Override
    public void addMilestoneToTask(Long requestingUserId, Long projectId, Long taskId, TaskMileStoneCreateRequest mileStoneCreateRequest){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 태스크 존재 여부 검증 후 find
        TaskEntity taskEntity = businessRuleValidator.findTaskOrThrow(taskId);
        // 태스크가 프로젝트에 속하는지 검증
        businessRuleValidator.validateTaskBelongsToProject(projectId, taskId);
        // 마일스톤 존재 여부 검증 후 find
        MilestoneEntity milestoneEntity = businessRuleValidator.findMilestoneOrThrow(mileStoneCreateRequest.mileStoneId());
        // 마일스톤이 프로젝트에 속하는지 검증
        businessRuleValidator.validateMilestoneBelongsToProject(projectId, milestoneEntity.getId());

        taskEntity.setMilestone(milestoneEntity);
        taskRepository.save(taskEntity);
    }

    @Transactional
    @Override
    public void removeMilestoneOnTask(Long requestingUserId, Long projectId, Long taskId, Long milestoneId){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 태스크 존재 여부 검증 후 find
        TaskEntity taskEntity = businessRuleValidator.findTaskOrThrow(taskId);
        // 태스크가 프로젝트에 속하는지 검증
        businessRuleValidator.validateTaskBelongsToProject(projectId, taskId);
        // 마일스톤 존재 여부 검증 후 find
        MilestoneEntity milestoneEntity = businessRuleValidator.findMilestoneOrThrow(milestoneId);
        // 마일스톤이 프로젝트에 속하는지 검증
        businessRuleValidator.validateMilestoneBelongsToProject(projectId, milestoneEntity.getId());
        // 마일스톤이 태스크에 속하는지 검증
        businessRuleValidator.validateMilestoneBelongsToTask(taskId, milestoneId);

        taskEntity.setMilestone(null);
        taskRepository.save(taskEntity);
    }

    //re-checked
    @Transactional
    @Override
    public void deleteTask(Long requestingUserId, Long projectId, Long taskId){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 태스크 존재 여부 검증 후 find
        TaskEntity taskEntity = businessRuleValidator.findTaskOrThrow(taskId);
        // 태스크가 프로젝트에 속하는지 검증
        businessRuleValidator.validateTaskBelongsToProject(projectId, taskId);

        taskEntity.setProject(null);
        taskEntity.setProjectMember(null);
        taskEntity.setMilestone(null);

        taskRepository.delete(taskEntity);
    }
}
