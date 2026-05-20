package com.nhnacademy.task.common.validator;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.InvalidRequestException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.entity.*;
import com.nhnacademy.task.repository.MilestoneRepository;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessRuleValidator {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final MilestoneRepository milestoneRepository;

    public ProjectEntity findProjectOrThrow(Long projectId){
        return projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                )
        );
    }

    public ProjectMemberEntity findProjectMemberOrThrow(Long projectMemberId){
        return projectMemberRepository.findById(projectMemberId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project member not found", projectMemberId)
                )
        );
    }

    public ProjectMemberEntity findProjectMemberOrThrow(Long projectId, Long userId){
        return projectMemberRepository.findByProject_IdAndUserId(projectId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s user not found in id:%s project", userId, projectId)
                )
        );
    }

    public TaskEntity findTaskOrThrow(Long taskId){
        return taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s task not found", taskId)
                )
        );
    }

    public MilestoneEntity findMilestoneOrThrow(Long milestoneId){
        return milestoneRepository.findById(milestoneId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s milestone not found", milestoneId)
                )
        );
    }

    public void validateProjectExists(Long projectId){
        if(!projectRepository.existsById(projectId)){
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }
    }

    public void validateProjectMembership(Long userId, Long projectId){
        if(!projectMemberRepository.existsByUserIdAndProject_Id(userId, projectId)){
            throw new NoPermissionException(
                    String.format("id:%s user has no membership permission for id:%s project",
                            userId, projectId)
            );
        }
    }

    public void validateProjectAdmin(Long userId, Long projectId){
        if(!projectMemberRepository.existsByProject_IdAndUserIdAndRole(projectId, userId, ProjectMemberRole.ADMIN)){
            throw new NoPermissionException(
                    String.format("id:%s user has no admin permission for id:%s project",
                            userId, projectId)
            );
        }
    }

    public void validateMemberIsNotAdmin(Long projectId, Long projectMemberId) {
        if(projectMemberRepository.existsByIdAndProject_IdAndRole(projectMemberId, projectId, ProjectMemberRole.ADMIN)) {
            throw new NoPermissionException(
                    String.format("id:%s admin project member cannot delete themselves from id:%s project", projectMemberId, projectId)
            );
        }
    }

    public void validateMemberBelongsToProject(Long projectId, Long projectMemberId){
        if(!projectMemberRepository.existsByIdAndProject_Id(projectMemberId, projectId)){
            throw new InvalidRequestException(
                    String.format("id:%s projectMember does not belong to projectId:%s",
                            projectMemberId, projectId)
            );
        }
    }

    public void validateTaskBelongsToProject(Long projectId, Long taskId){
        if(!taskRepository.existsByProject_IdAndId(projectId, taskId)){
            throw new InvalidRequestException(
                    String.format("id:%s task does not belong to id:%s project", taskId, projectId)
            );
        }
    }

    public void validateMilestoneBelongsToProject(Long projectId, Long milestoneId){
        if(!milestoneRepository.existsByProject_IdAndId(projectId, milestoneId)){
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s project", milestoneId, projectId)
            );
        }
    }

    public void validateMilestoneBelongsToTask(Long taskId, Long milestoneId){
        if(!taskRepository.existsByMilestone_IdAndId(milestoneId, taskId)){
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s task", milestoneId, taskId)
            );
        }
    }

    public void validateStartAndEndDates(LocalDateTime startDate, LocalDateTime endDate){
        if(endDate.isBefore(startDate)){
            throw new InvalidRequestException(
                    String.format(
                            "Invalid date range: startDate=%s must be before endDate=%s",
                            startDate, endDate
                    )
            );
        }
    }






}
