package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.InvalidRequestException;
import com.nhnacademy.task.common.exception.NoPermissionException;
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
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MilestoneRepository milestoneRepository;

    //re-checked
    @Transactional
    @Override
    public void createTask(Long requestingUserId, Long projectId, TaskCreateRequest taskCreateRequest){
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
        ));

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProject_IdAndUserId(projectId, requestingUserId)
                .orElseThrow(() -> new NoPermissionException(
                        String.format("id:%s user has no user permission for id:%s project",
                                requestingUserId, projectId)
                ));

        TaskEntity taskEntity = new TaskEntity(taskCreateRequest.name(), projectEntity, projectMemberEntity, taskCreateRequest.content());
        projectEntity.addTask(taskEntity);
        projectMemberEntity.addTask(taskEntity);
        taskRepository.save(taskEntity);
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public TaskSimpleResponse getTaskSimpleInfoById(Long requestingUserId, Long projectId, Long taskId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if (!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)) {
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s task not found", taskId)
        ));

        if (!taskEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s task does not belong to id:%s project", taskId, projectId)
            );
        }

        return new TaskSimpleResponse(
                taskEntity.getId(),
                taskEntity.getProjectMember().getId(),
                taskEntity.getName(),
                taskEntity.getMilestone() != null ?
                        MileStoneSimpleResponse.from(taskEntity.getMilestone()) : null,
                taskEntity.getTaskTagList() != null ?
                        taskEntity.getTaskTagList().stream().map(taskTagEntity -> {
                            return new TagResponse(taskTagEntity.getTag().getId(), taskTagEntity.getTag().getName());
                        }).toList() : new ArrayList<>()
        );
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public TaskDetailResponse getTaskDetailInfoById(Long requestingUserId, Long projectId, Long taskId){
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if (!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)) {
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException(
                String.format("id:%s task not found", taskId)
        ));

        if (!taskEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s task does not belong to id:%s project", taskId, projectId)
            );
        }

        return new TaskDetailResponse(
                taskEntity.getId(),
                taskEntity.getProject().getId(),
                taskEntity.getProjectMember().getId(),
                taskEntity.getMilestone() != null ? new MileStoneDetailResponse(
                        taskEntity.getMilestone().getId(),
                        taskEntity.getMilestone().getName(),
                        taskEntity.getMilestone().getStartDate(),
                        taskEntity.getMilestone().getEndDate(),
                        taskEntity.getMilestone().getCreatedAt()
                ) : null,
                taskEntity.getName(),
                taskEntity.getContent(),
                taskEntity.getCreatedAt(),
                taskEntity.getTaskTagList() != null ?
                        taskEntity.getTaskTagList().stream().map(taskTagEntity -> {
                            return new TagResponse(taskTagEntity.getTag().getId(), taskTagEntity.getTag().getName());
                        }).toList() : new ArrayList<>(),
                taskEntity.getCommentList() != null ?
                        taskEntity.getCommentList().stream().map(commentEntity -> {
                            return new CommentResponse(
                                    commentEntity.getId(),
                                    commentEntity.getProjectMember().getId(),
                                    commentEntity.getContent(),
                                    commentEntity.getCreatedAt(),
                                    commentEntity.getUpdatedAt()
                            );
                        }).toList() : new ArrayList<>()
        );
    }

    //re-checked
    @Transactional(readOnly = true)
    @Override
    public List<TaskSimpleResponse> getTasksByProjectID(Long requestingUserId, Long projectId){
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if (!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)) {
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        List<TaskEntity> allByProjectId = taskRepository.findAllByProject_Id(projectId);
        if(allByProjectId != null){
            List<TaskSimpleResponse> taskSimpleResponses = new ArrayList<>();
            for (TaskEntity taskEntity : allByProjectId) {
                taskSimpleResponses.add(
                        new TaskSimpleResponse(
                                taskEntity.getId(),
                                taskEntity.getProjectMember().getId(),
                                taskEntity.getName(),
                                taskEntity.getMilestone() != null ?
                                        MileStoneSimpleResponse.from(taskEntity.getMilestone()) : null,
                                taskEntity.getTaskTagList() != null ?
                                        taskEntity.getTaskTagList().stream().map(taskTagEntity -> {
                                            return new TagResponse(
                                                    taskTagEntity.getTag().getId(),
                                                    taskTagEntity.getTag().getName()
                                            );
                                        }).toList() : new ArrayList<>()
                        )
                );
            }
            return taskSimpleResponses;
        }
        return new ArrayList<>();
    }

    //re-checked
    @Transactional
    @Override
    public void updateTask(Long requestingUserId, Long projectId, Long taskId, TaskUpdateRequest taskUpdateRequest){
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException(
                    String.format("id:%s project not found", projectId)
            );
        }

        if (!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)) {
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s task not found", taskId)
                )
        );

        if (!taskEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s task does not belong to id:%s project", taskId, projectId)
            );
        }

        taskEntity.updateNameAndContent(taskUpdateRequest.name(), taskUpdateRequest.content());
        taskRepository.save(taskEntity);
    }

    @Transactional
    @Override
    public void addMilestoneToTask(Long requestingUserId, Long projectId, Long taskId, TaskMileStoneCreateRequest mileStoneCreateRequest){
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                ));

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProject_IdAndUserId(projectId, requestingUserId).orElseThrow(
                () -> new NoPermissionException(
                        String.format("id:%s user has no user permission for id:%s project",
                                requestingUserId, projectId)
                ));

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s task not found", taskId)
                )
        );

        if (!taskEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s task does not belong to id:%s project", taskId, projectId)
            );
        }

        MilestoneEntity milestoneEntity = milestoneRepository.findById(mileStoneCreateRequest.mileStoneId()).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s milestone not found", mileStoneCreateRequest.mileStoneId())
                )
        );

        if (!milestoneEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s project", mileStoneCreateRequest.mileStoneId(), projectId)
            );
        }

        taskEntity.setMilestone(milestoneEntity);
        taskRepository.save(taskEntity);
    }

    @Transactional
    @Override
    public void removeMilestoneOnTask(Long requestingUserId, Long projectId, Long taskId, Long milestoneId){
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                ));

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProject_IdAndUserId(projectId, requestingUserId).orElseThrow(
                () -> new NoPermissionException(
                        String.format("id:%s user has no user permission for id:%s project",
                                requestingUserId, projectId)
                ));

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s task not found", taskId)
                )
        );

        if (!taskEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s task does not belong to id:%s project", taskId, projectId)
            );
        }

        MilestoneEntity milestoneEntity = milestoneRepository.findById(milestoneId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s milestone not found", milestoneId)
                )
        );

        if(!milestoneEntity.getProject().getId().equals(projectId)){
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s project", milestoneId, projectId)
            );
        }

        if(taskEntity.getMilestone() == null || !taskEntity.getMilestone().getId().equals(milestoneId)){
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s task",
                            milestoneId, taskId)
            );
        }

        taskEntity.setMilestone(null);
        taskRepository.save(taskEntity);
    }

    //re-checked
    @Transactional
    @Override
    public void deleteTask(Long requestingUserId, Long projectId, Long taskId){
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                ));

        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProject_IdAndUserId(projectId, requestingUserId).orElseThrow(
                () -> new NoPermissionException(
                        String.format("id:%s user has no user permission for id:%s project",
                                requestingUserId, projectId)
                ));

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s task not found", taskId)
                )
        );

        if (!taskEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s task does not belong to id:%s project", taskId, projectId)
            );
        }

        taskEntity.setProject(null);
        taskEntity.setProjectMember(null);
        taskEntity.setMilestone(null);

        taskRepository.delete(taskEntity);
    }
}
