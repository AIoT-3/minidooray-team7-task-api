package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.CommentCreateRequest;
import com.nhnacademy.task.dto.req.CommentUpdateRequest;
import com.nhnacademy.task.dto.resp.CommentResponse;
import com.nhnacademy.task.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 생성 성공")
    void createComment() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        CommentCreateRequest request = new CommentCreateRequest("New Comment");
        CommentResponse response = new CommentResponse(1L, 1L, "New Comment", LocalDateTime.now(), LocalDateTime.now());

        when(commentService.createComment(eq(projectId), eq(taskId), eq(requestingUserId), any(String.class))).thenReturn(response);

        mockMvc.perform(post("/projects/{projectId}/tasks/{taskId}/comments", projectId, taskId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("New Comment"));
    }

    @Test
    @DisplayName("태스크의 댓글 목록 조회 성공")
    void getComments() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        CommentResponse response = new CommentResponse(1L, 2L, "Comment", LocalDateTime.now(), LocalDateTime.now());

        when(commentService.getComments(projectId, taskId)).thenReturn(List.of(response));

        mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}/comments", projectId, taskId)
                        .header("X-USER-ID", requestingUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Comment"));
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;
        CommentUpdateRequest request = new CommentUpdateRequest("Updated Comment");
        CommentResponse response = new CommentResponse(1L, 1L, "Updated Comment", LocalDateTime.now(), LocalDateTime.now());

        when(commentService.updateComment(eq(projectId), eq(taskId), eq(commentId), eq(requestingUserId), any(String.class))).thenReturn(response);

        mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}/comments/{commentId}", projectId, taskId, commentId)
                        .header("X-USER-ID", requestingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated Comment"));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment() throws Exception {
        Long requestingUserId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;
        Long commentId = 1L;

        doNothing().when(commentService).deleteComment(projectId, taskId, commentId, requestingUserId);

        mockMvc.perform(delete("/projects/{projectId}/tasks/{taskId}/comments/{commentId}", projectId, taskId, commentId)
                        .header("X-USER-ID", requestingUserId))
                .andExpect(status().isNoContent());
    }
}
