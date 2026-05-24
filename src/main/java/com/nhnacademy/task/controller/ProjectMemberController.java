package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.ProjectMemberCreateRequest;
import com.nhnacademy.task.dto.resp.ProjectMemberResponse;
import com.nhnacademy.task.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/members")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<Void> addProjectMember(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @Valid @RequestBody ProjectMemberCreateRequest projectMemberCreateRequest
            ){
        projectMemberService.createProjectMember(requestingUserId, projectId, projectMemberCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId
    ){
        List<ProjectMemberResponse> members = projectMemberService.getAllByProjectId(requestingUserId, projectId);
        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteProjectMember(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "memberId") Long memberId
    ){
        projectMemberService.deleteProjectMemberByProjectIdAndProjectMemberId(
                requestingUserId,
                projectId,
                memberId
        );
        return ResponseEntity.noContent().build();
    }

}
