package com.nhnacademy.task.service;

import com.nhnacademy.task.dto.req.MileStoneCreateRequest;
import com.nhnacademy.task.dto.req.MileStoneUpdateRequest;
import com.nhnacademy.task.dto.resp.MileStoneDetailResponse;
import com.nhnacademy.task.dto.resp.MileStoneSimpleResponse;

import java.util.List;

public interface MilestoneService {
    //create
    void createMilestoneToProject(Long requestingUserId, Long projectId, MileStoneCreateRequest mileStoneCreateRequest);

    //read
    MileStoneDetailResponse getMilestoneDetailInfoById(Long requestingUserId, Long projectId, Long milestoneId);
    List<MileStoneSimpleResponse> getMilestoneSimpleInfosByProjectId(Long requestingUserId, Long projectId);

    //update
    void updateMilestone(Long requestingUserId, Long projectId, Long milestoneId, MileStoneUpdateRequest mileStoneUpdateRequest);

    //delete
    void deleteMilestoneOnProject(Long requestingUserId, Long projectId, Long milestoneId);

}
