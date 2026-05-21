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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CommentControllerTest
 *
 * @author chosun-nhn12
 * @since 26. 5. 19.
 */
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CommentService commentService;


    @Test
    @DisplayName("댓글 목록 조회")
    void getComments() throws Exception{
        Long projectId = 1L;
        Long taskId = 10L;

        CommentResponse response = new CommentResponse(
                100L,
                5L,
                "댓글 내용",
                LocalDateTime.of(2026, 5, 19, 10, 0),
                LocalDateTime.of(2026, 5, 19, 10, 0)
        );

        given(commentService.getComments(projectId, taskId))
                .willReturn(List.of(response));

        mockMvc.perform(get("/projects/{project-id}/tasks/{task-id}/comments", projectId, taskId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].projectMemberId").value(5))
                .andExpect(jsonPath("$[0].content").value("댓글 내용"));

        then(commentService).should().getComments(projectId, taskId);
    }

    @Test
    @DisplayName("댓글 생성")
    void createComment() throws Exception {
        Long projectId = 1L;
        Long taskId = 10L;
        Long projectMemberId = 5L;

        CommentCreateRequest request = new CommentCreateRequest("새 댓글");

        CommentResponse response = new CommentResponse(
                100L,
                projectMemberId,
                "새 댓글",
                LocalDateTime.of(2026, 5, 19, 10, 0),
                LocalDateTime.of(2026, 5, 19, 10, 0)
        );

        given(commentService.createComment(taskId, projectMemberId, request.content()))
                .willReturn(response);

        mockMvc.perform(post("/projects/{project-id}/tasks/{task-id}/comments", projectId, taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", projectMemberId)
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.projectMemberId").value(5))
                .andExpect(jsonPath("$.content").value("새 댓글"));

        then(commentService).should().createComment(taskId, projectMemberId, request.content());
    }

    @Test
    @DisplayName("댓글 수정")
    void modifyComment() throws Exception {
        Long projectId = 1L;
        Long taskId = 10L;
        Long commentId = 100L;
        Long projectMemberId = 5L;

        CommentUpdateRequest request = new CommentUpdateRequest("수정된 댓글");


        CommentResponse response = new CommentResponse(
                commentId,
                projectMemberId,
                "수정된 댓글",
                LocalDateTime.of(2026, 5, 19, 10, 0),
                LocalDateTime.of(2026, 5, 19, 10, 0)
        );

        given(commentService.updateComment(projectId, taskId, commentId, projectMemberId, request.content()))
                .willReturn(response);

        mockMvc.perform(
                    put("/projects/{project-id}/tasks/{task-id}/comments/{comment-id}",
                                projectId, taskId, commentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-USER-ID", projectMemberId)
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON)
                    )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.projectMemberId").value(projectMemberId))
                .andExpect(jsonPath("$.content").value("수정된 댓글"));

        then(commentService)
                .should()
                .updateComment(projectId, taskId, commentId, projectMemberId, request.content());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() throws Exception{
        Long projectId = 1L;
        Long taskId = 10L;
        Long commentId = 100L;
        Long projectMemberId = 5L;

        mockMvc.perform(
                    delete("/projects/{project-id}/tasks/{task-id}/comments/{comment-id}",
                            projectId, taskId, commentId)
                    .header("X-USER-ID", projectMemberId))
                .andExpect(status().isNoContent());

        then(commentService)
                .should()
                .deleteComment(projectId, taskId, commentId, projectMemberId);

    }
}