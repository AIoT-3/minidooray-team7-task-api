package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.ProjectCreateRequest;
import com.nhnacademy.task.dto.req.ProjectUpdateRequest;
import com.nhnacademy.task.dto.resp.ProjectSimpleResponse;
import com.nhnacademy.task.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Void> createProject(
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @Valid @RequestBody ProjectCreateRequest projectCreateRequest
            ){
        projectService.createProject(requestingUserId, projectCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProjectSimpleResponse>> getMyProjects(
            @RequestHeader("X-USER-ID") Long requestingUserId
    ){
        List<ProjectSimpleResponse> projectsByUserId = projectService.getProjectsByUserId(requestingUserId);
        return ResponseEntity.ok(projectsByUserId);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @PathVariable(name = "projectId") Long projectId,
            @RequestHeader("X-USER-ID") Long requestingUserId,
            @Valid @RequestBody ProjectUpdateRequest request
    ){
        projectService.updateProject(requestingUserId, projectId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable(name = "projectId") Long projectId,
            @RequestHeader("X-USER-ID") Long requestingUserId
    ){
        projectService.deleteProject(requestingUserId, projectId);
        return ResponseEntity.noContent().build();
    }

}
