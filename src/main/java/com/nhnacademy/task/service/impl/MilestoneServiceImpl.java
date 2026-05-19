package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.InvalidRequestException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.dto.req.MileStoneCreateRequest;
import com.nhnacademy.task.dto.req.MileStoneUpdateRequest;
import com.nhnacademy.task.dto.resp.MileStoneDetailResponse;
import com.nhnacademy.task.dto.resp.MileStoneSimpleResponse;
import com.nhnacademy.task.entity.MilestoneEntity;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectMemberEntity;
import com.nhnacademy.task.entity.TaskEntity;
import com.nhnacademy.task.repository.MilestoneRepository;
import com.nhnacademy.task.repository.ProjectMemberRepository;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    @Override
    public void createMilestoneToProject(Long requestingUserId, Long projectId, MileStoneCreateRequest mileStoneCreateRequest){
        if(mileStoneCreateRequest.endDate().isBefore(mileStoneCreateRequest.startDate())){
            throw new InvalidRequestException(
                    String.format(
                            "Invalid milestone date range: startDate=%s must be before endDate=%s",
                            mileStoneCreateRequest.startDate(),
                            mileStoneCreateRequest.endDate()
                    )
            );
        }

        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                ));

        if (!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)) {
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        MilestoneEntity milestoneEntity = new MilestoneEntity(
                mileStoneCreateRequest.name(),
                projectEntity,
                mileStoneCreateRequest.startDate(),
                mileStoneCreateRequest.endDate());

        projectEntity.addMilestone(milestoneEntity);
        milestoneRepository.save(milestoneEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public MileStoneDetailResponse getMilestoneDetailInfoById(Long requestingUserId, Long projectId, Long milestoneId){
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

        MilestoneEntity milestoneEntity = milestoneRepository.findById(milestoneId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s milestone not found", projectId)
                ));

        if (!milestoneEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s project", milestoneId, projectId)
            );
        }

        return new MileStoneDetailResponse(
                milestoneEntity.getId(),
                milestoneEntity.getName(),
                milestoneEntity.getStartDate(),
                milestoneEntity.getStartDate(),
                milestoneEntity.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<MileStoneSimpleResponse> getMilestoneSimpleInfosByProjectId(Long requestingUserId, Long projectId){
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

        List<MilestoneEntity> allByProjectId = milestoneRepository.findAllByProject_Id(projectId);
        if(allByProjectId!=null){
            List<MileStoneSimpleResponse> mileStoneSimpleResponses = new ArrayList<>();
            for (MilestoneEntity milestoneEntity : allByProjectId) {
                mileStoneSimpleResponses.add(
                        new MileStoneSimpleResponse(
                                milestoneEntity.getId(),
                                milestoneEntity.getName()
                        )
                );
            }
            return mileStoneSimpleResponses;
        }
        return new ArrayList<>();
    }

    @Transactional
    @Override
    public void updateMilestone(Long requestingUserId, Long projectId, Long milestoneId, MileStoneUpdateRequest mileStoneUpdateRequest){
        if(mileStoneUpdateRequest.endDate().isBefore(mileStoneUpdateRequest.startDate())){
            throw new InvalidRequestException(
                    String.format(
                            "Invalid milestone date range: startDate=%s must be before endDate=%s",
                            mileStoneUpdateRequest.startDate(),
                            mileStoneUpdateRequest.endDate()
                    )
            );
        }

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

        MilestoneEntity milestoneEntity = milestoneRepository.findById(milestoneId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s milestone not found", projectId)
                ));

        if (!milestoneEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s project", milestoneId, projectId)
            );
        }

        milestoneEntity.updateNameAndStartDateAndEndDate(
                mileStoneUpdateRequest.name(),
                mileStoneUpdateRequest.startDate(),
                mileStoneUpdateRequest.endDate()
        );

        milestoneRepository.save(milestoneEntity);
    }

    @Transactional
    @Override
    public void deleteMilestoneOnProject(Long requestingUserId, Long projectId, Long milestoneId){
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("id:%s project not found", projectId)
                ));

        if (!projectMemberRepository.existsByUserIdAndProject_Id(requestingUserId, projectId)) {
            throw new NoPermissionException(
                    String.format("id:%s user has no user permission for id:%s project",
                            requestingUserId, projectId)
            );
        }

        MilestoneEntity milestoneEntity = milestoneRepository.findById(milestoneId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("id:%s milestone not found", projectId)
                ));

        if (!milestoneEntity.getProject().getId().equals(projectId)) {
            throw new InvalidRequestException(
                    String.format("id:%s milestone does not belong to id:%s project", milestoneId, projectId)
            );
        }

        for (TaskEntity task : new ArrayList<>(milestoneEntity.getTaskList())) {
            task.setMilestone(null);
        }

        projectEntity.getMilestoneList().remove(milestoneEntity);
        milestoneEntity.setProject(null);

        milestoneRepository.save(milestoneEntity);
    }

}
