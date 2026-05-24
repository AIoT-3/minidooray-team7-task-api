package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.ProjectMemberCreateRequest;
import com.nhnacademy.task.dto.resp.ProjectMemberResponse;
import com.nhnacademy.task.service.ProjectMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectMemberController.class)
class ProjectMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectMemberService projectMemberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("프로젝트 멤버 추가 성공")
    void addProjectMember() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectMemberCreateRequest request = new ProjectMemberCreateRequest(2L);

        doNothing().when(projectMemberService).createProjectMember(eq(requestingUserId), eq(projectId), any(ProjectMemberCreateRequest.class));

        mockMvc.perform(post("/projects/{projectId}/members", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("프로젝트 멤버 목록 조회 성공")
    void getProjectMembers() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        ProjectMemberResponse response = new ProjectMemberResponse(1L, 2L, "MEMBER", false);

        when(projectMemberService.getAllByProjectId(requestingUserId, projectId)).thenReturn(List.of(response));

        mockMvc.perform(get("/projects/{projectId}/members", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(2L))
                .andExpect(jsonPath("$[0].role").value("MEMBER"));
    }

    @Test
    @DisplayName("프로젝트 멤버 삭제 성공")
    void deleteProjectMember() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long memberId = 1L;

        doNothing().when(projectMemberService).deleteProjectMemberByProjectIdAndProjectMemberId(requestingUserId, projectId, memberId);

        mockMvc.perform(delete("/projects/{projectId}/members/{memberId}", projectId, memberId)
                        .header("X-USER-ID", requestingUserId))
                .andExpect(status().isNoContent());
    }
}
