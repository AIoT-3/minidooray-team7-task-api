package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.ProjectCreateRequest;
import com.nhnacademy.task.dto.req.ProjectUpdateRequest;
import com.nhnacademy.task.dto.resp.ProjectDetailResponse;
import com.nhnacademy.task.dto.resp.ProjectSimpleResponse;
import com.nhnacademy.task.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("프로젝트 생성 성공")
    void createProject() throws Exception {
        Long requestingUserId = 1L;
        ProjectCreateRequest request = new ProjectCreateRequest("New Project");

        doNothing().when(projectService).createProject(eq(requestingUserId), any(ProjectCreateRequest.class));

        mockMvc.perform(post("/projects")
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("유저의 프로젝트 목록 조회 성공")
    void getMyProjects() throws Exception {
        Long requestingUserId = 1L;
        ProjectSimpleResponse response = new ProjectSimpleResponse(1L, "Project", "ACTIVE", LocalDateTime.now());

        when(projectService.getProjectsByUserId(requestingUserId)).thenReturn(List.of(response));

        mockMvc.perform(get("/projects")
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Project"))
                .andExpect(jsonPath("$[0].state").value("ACTIVE"));
    }

    @Test
    @DisplayName("프로젝트 상세 조회 성공")
    void getProject() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectDetailResponse response = new ProjectDetailResponse(
                1L, "Project", "ACTIVE", LocalDateTime.now(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );

        when(projectService.getProjectDetailInfoById(requestingUserId, projectId)).thenReturn(response);

        mockMvc.perform(get("/projects/{projectId}", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Project"))
                .andExpect(jsonPath("$.state").value("ACTIVE"));
    }

    @Test
    @DisplayName("프로젝트 수정 성공")
    void updateProject() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectUpdateRequest request = new ProjectUpdateRequest("Updated Project", "DORMANT");

        doNothing().when(projectService).updateProject(eq(requestingUserId), eq(projectId), any(ProjectUpdateRequest.class));

        mockMvc.perform(put("/projects/{projectId}", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("프로젝트 삭제 성공")
    void deleteProject() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;

        doNothing().when(projectService).deleteProject(requestingUserId, projectId);

        mockMvc.perform(delete("/projects/{projectId}", projectId)
                        .header("X-USER-ID", requestingUserId))
                .andExpect(status().isNoContent());
    }
}
