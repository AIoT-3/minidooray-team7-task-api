package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.MileStoneCreateRequest;
import com.nhnacademy.task.dto.req.MileStoneUpdateRequest;
import com.nhnacademy.task.dto.resp.MileStoneDetailResponse;
import com.nhnacademy.task.dto.resp.MileStoneSimpleResponse;
import com.nhnacademy.task.service.MilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/milestones")
public class MilestoneController {
    private final MilestoneService milestoneService;

    @PostMapping
    public ResponseEntity<Void> createMilestone(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @Valid @RequestBody MileStoneCreateRequest mileStoneCreateRequest
            ) {
        milestoneService.createMilestoneToProject(requestingUserId,projectId,mileStoneCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<MileStoneSimpleResponse>> getMilestonesByProject(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId
    ) {
        List<MileStoneSimpleResponse> milestones = milestoneService.getMilestoneSimpleInfosByProjectId(requestingUserId, projectId);
        return ResponseEntity.ok(milestones);
    }

    @GetMapping("/{milestoneId}")
    public ResponseEntity<MileStoneDetailResponse> getMilestoneById(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "milestoneId") Long milestoneId
    ) {
        MileStoneDetailResponse milestoneDetail
                = milestoneService.getMilestoneDetailInfoById(
                        requestingUserId, projectId, milestoneId);
        return ResponseEntity.ok(milestoneDetail);
    }

    @PutMapping("/{milestoneId}")
    public ResponseEntity<Void> updateMilestone(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "milestoneId") Long milestoneId,
            @Valid @RequestBody MileStoneUpdateRequest mileStoneUpdateRequest
    ) {
        milestoneService.updateMilestone(requestingUserId,projectId,milestoneId,mileStoneUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "milestoneId") Long milestoneId
    ) {
        milestoneService.deleteMilestoneOnProject(requestingUserId,projectId,milestoneId);
        return ResponseEntity.noContent().build();
    }


}
