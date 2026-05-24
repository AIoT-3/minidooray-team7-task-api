package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.MileStoneCreateRequest;
import com.nhnacademy.task.dto.req.MileStoneUpdateRequest;
import com.nhnacademy.task.dto.resp.MileStoneDetailResponse;
import com.nhnacademy.task.dto.resp.MileStoneSimpleResponse;
import com.nhnacademy.task.service.MilestoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MilestoneController.class)
class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MilestoneService milestoneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("마일스톤 생성 성공")
    void createMilestone() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        MileStoneCreateRequest request = new MileStoneCreateRequest("New Milestone", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        doNothing().when(milestoneService).createMilestoneToProject(eq(requestingUserId), eq(projectId), any(MileStoneCreateRequest.class));

        mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("프로젝트의 마일스톤 목록 조회 성공")
    void getMilestonesByProject() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        MileStoneSimpleResponse response = new MileStoneSimpleResponse(1L, "Milestone", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        when(milestoneService.getMilestoneSimpleInfosByProjectId(requestingUserId, projectId)).thenReturn(List.of(response));

        mockMvc.perform(get("/projects/{projectId}/milestones", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Milestone"));
    }

    @Test
    @DisplayName("마일스톤 상세 조회 성공")
    void getMilestoneById() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long milestoneId = 1L;
        MileStoneDetailResponse response = new MileStoneDetailResponse(1L, "Milestone", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now());

        when(milestoneService.getMilestoneDetailInfoById(requestingUserId, projectId, milestoneId)).thenReturn(response);

        mockMvc.perform(get("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId)
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Milestone"));
    }

    @Test
    @DisplayName("마일스톤 수정 성공")
    void updateMilestone() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long milestoneId = 1L;
        MileStoneUpdateRequest request = new MileStoneUpdateRequest("Updated Milestone", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        doNothing().when(milestoneService).updateMilestone(eq(requestingUserId), eq(projectId), eq(milestoneId), any(MileStoneUpdateRequest.class));

        mockMvc.perform(put("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("마일스톤 삭제 성공")
    void deleteMilestone() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long milestoneId = 1L;

        doNothing().when(milestoneService).deleteMilestoneOnProject(requestingUserId, projectId, milestoneId);

        mockMvc.perform(delete("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId)
                        .header("X-USER-ID", requestingUserId))
                .andExpect(status().isNoContent());
    }
}
