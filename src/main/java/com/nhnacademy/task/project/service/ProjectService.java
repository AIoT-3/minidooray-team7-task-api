package com.nhnacademy.task.project.service;

import com.nhnacademy.task.project.dto.ProjectCreateRequestDto;
import com.nhnacademy.task.project.dto.ProjectSimpleResponseDto;
import com.nhnacademy.task.project.dto.ProjectUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ProjectService
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public interface ProjectService {
    //create
    void createProject(Long requestingUserId, ProjectCreateRequestDto projectCreateRequestDto);

    //read
    ProjectSimpleResponseDto getProjectById(Long requestingUserId, Long projectId);
    Page<ProjectSimpleResponseDto> getProjectsPageByUserId(Long requestingUserId, Pageable pageable);

    //update
    void updateProject(Long requestingUserId, Long projectId, ProjectUpdateRequestDto projectUpdateRequestDto);

    //delete
    void deleteProject(Long requestingUserId, Long projectId);
}
