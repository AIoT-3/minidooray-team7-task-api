package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.common.exception.InvalidRequestException;
import com.nhnacademy.task.common.exception.NoPermissionException;
import com.nhnacademy.task.common.validator.BusinessRuleValidator;
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

    private final BusinessRuleValidator businessRuleValidator;

    @Transactional
    @Override
    public void createMilestoneToProject(Long requestingUserId, Long projectId, MileStoneCreateRequest mileStoneCreateRequest){
        // 마일스톤 시작일, 종료일 체크
        businessRuleValidator.validateStartAndEndDates(
                mileStoneCreateRequest.startDate(),
                mileStoneCreateRequest.endDate()
        );
        // 프로젝트 존재 여부 검증 후 find
        ProjectEntity projectEntity = businessRuleValidator.findProjectOrThrow(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);

        MilestoneEntity milestoneEntity = MilestoneEntity.create(
                mileStoneCreateRequest,
                projectEntity
        );

        projectEntity.addMilestone(milestoneEntity);
        milestoneRepository.save(milestoneEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public MileStoneDetailResponse getMilestoneDetailInfoById(Long requestingUserId, Long projectId, Long milestoneId){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 마일스톤 존재 여부 검증 후 find
        MilestoneEntity milestoneEntity = businessRuleValidator.findMilestoneOrThrow(milestoneId);
        // 마일스톤이 해당 프로젝트에 속하는 지 검증
        businessRuleValidator.validateMilestoneBelongsToProject(projectId, milestoneId);

        return MileStoneDetailResponse.from(milestoneEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MileStoneSimpleResponse> getMilestoneSimpleInfosByProjectId(Long requestingUserId, Long projectId){
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);

        List<MilestoneEntity> allByProjectId = milestoneRepository.findAllByProject_Id(projectId);
        return allByProjectId != null ?
                allByProjectId.stream().map(
                        MileStoneSimpleResponse::from
                ).toList() : new ArrayList<>();
    }

    @Transactional
    @Override
    public void updateMilestone(Long requestingUserId, Long projectId, Long milestoneId, MileStoneUpdateRequest mileStoneUpdateRequest){
        // 마일스톤 시작일, 종료일 체크
        businessRuleValidator.validateStartAndEndDates(
                mileStoneUpdateRequest.startDate(),
                mileStoneUpdateRequest.endDate()
        );
        // 프로젝트 존재 여부 검증
        businessRuleValidator.validateProjectExists(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 마일스톤 존재 여부 검증 후 find
        MilestoneEntity milestoneEntity = businessRuleValidator.findMilestoneOrThrow(milestoneId);
        // 마일스톤이 해당 프로젝트에 속하는 지 검증
        businessRuleValidator.validateMilestoneBelongsToProject(projectId, milestoneId);

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
        // 프로젝트 존재 여부 검증 후 find
        ProjectEntity projectEntity = businessRuleValidator.findProjectOrThrow(projectId);
        // 요청자가 프로젝트 멤버 인지 체크
        businessRuleValidator.validateProjectMembership(requestingUserId, projectId);
        // 마일스톤 존재 여부 검증 후 find
        MilestoneEntity milestoneEntity = businessRuleValidator.findMilestoneOrThrow(milestoneId);
        // 마일스톤이 해당 프로젝트에 속하는 지 검증
        businessRuleValidator.validateMilestoneBelongsToProject(projectId, milestoneId);


        for (TaskEntity task : new ArrayList<>(milestoneEntity.getTaskList())) {
            task.setMilestone(null);
        }
        projectEntity.getMilestoneList().remove(milestoneEntity);
        milestoneEntity.setProject(null);

        milestoneRepository.save(milestoneEntity);
    }

}
