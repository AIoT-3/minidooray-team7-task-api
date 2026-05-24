package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.TaskCreateRequest;
import com.nhnacademy.task.dto.req.TaskMileStoneCreateRequest;
import com.nhnacademy.task.dto.req.TaskUpdateRequest;
import com.nhnacademy.task.dto.resp.TaskDetailResponse;
import com.nhnacademy.task.dto.resp.TaskSimpleResponse;
import com.nhnacademy.task.service.TaskService;
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

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("태스크 생성 성공")
    void createTask() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        TaskCreateRequest request = new TaskCreateRequest("New Task", "Task Content");

        doNothing().when(taskService).createTask(eq(requestingUserId), eq(projectId), any(TaskCreateRequest.class));

        mockMvc.perform(post("/projects/{projectId}/tasks", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("프로젝트의 태스크 목록 조회 성공")
    void getTasksByProject() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        TaskSimpleResponse response = new TaskSimpleResponse(1L, 2L, "Task", null, Collections.emptyList());

        when(taskService.getTasksByProjectID(requestingUserId, projectId)).thenReturn(List.of(response));

        mockMvc.perform(get("/projects/{projectId}/tasks", projectId)
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Task"));
    }

    @Test
    @DisplayName("태스크 상세 조회 성공")
    void getTaskById() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        TaskDetailResponse response = new TaskDetailResponse(
                1L, 1L, 2L, null, "Task", "Content", LocalDateTime.now(), Collections.emptyList(), Collections.emptyList()
        );

        when(taskService.getTaskDetailInfoById(requestingUserId, projectId, taskId)).thenReturn(response);

        mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Task"))
                .andExpect(jsonPath("$.content").value("Content"));
    }

    @Test
    @DisplayName("태스크 수정 성공")
    void updateTask() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest("Updated Task", "Updated Content");

        doNothing().when(taskService).updateTask(eq(requestingUserId), eq(projectId), eq(taskId), any(TaskUpdateRequest.class));

        mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("태스크에 마일스톤 추가 성공")
    void addMilestoneToTask() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        TaskMileStoneCreateRequest request = new TaskMileStoneCreateRequest(2L);

        doNothing().when(taskService).addMilestoneToTask(eq(requestingUserId), eq(projectId), eq(taskId), any(TaskMileStoneCreateRequest.class));

        mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}/milestones", projectId, taskId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("태스크의 마일스톤 제거 성공")
    void removeMilestoneOnTask() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        Long milestoneId = 2L;

        doNothing().when(taskService).removeMilestoneOnTask(requestingUserId, projectId, taskId, milestoneId);

        mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}/milestones/{milestoneId}", projectId, taskId, milestoneId)
                        .header("X-USER-ID", requestingUserId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("태스크 삭제 성공")
    void deleteTask() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;

        doNothing().when(taskService).deleteTask(requestingUserId, projectId, taskId);

        mockMvc.perform(delete("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                        .header("X-USER-ID", requestingUserId))
                .andExpect(status().isNoContent());
    }
}
