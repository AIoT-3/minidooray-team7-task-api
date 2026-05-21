package com.nhnacademy.task.controller;


import com.nhnacademy.task.dto.req.TaskTagCreateRequest;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.service.TaskTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TaskTagControllerTest
 *
 * @author chosun-nhn12
 * @since 26. 5. 21.
 */
@WebMvcTest(TaskTagController.class)
class TaskTagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TaskTagService service;


    @Test
    void getTaskTags() throws Exception {
        Long projectId = 1L;
        Long taskId = 10L;

        TagResponse response = new TagResponse(100L, "hi");

        when(service.getTaskTags(projectId, taskId))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/projects/{project-id}/tasks/{task-id}/tags", projectId, taskId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("hi"));

        then(service).should().getTaskTags(projectId, taskId);
    }

    @Test
    void attachTaskTags() throws Exception {
        Long projectId = 1L;
        Long taskId = 10L;

        Long tagId = 1L;
        Long tagId2 = 2L;
        String name = "hi";
        String name2 = "hi2";

        TaskTagCreateRequest request = new TaskTagCreateRequest(List.of(1L, 2L));

        when(service.attachTag(projectId, taskId, tagId)).thenReturn(new TagResponse(tagId, name));
        when(service.attachTag(projectId, taskId, tagId2)).thenReturn(new TagResponse(tagId2, name2));

        mockMvc.perform(post("/projects/{project-id}/tasks/{task-id}/tags", projectId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("hi"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("hi2"));

        then(service).should().attachTag(projectId, taskId, 1L);
        then(service).should().attachTag(projectId, taskId, 2L);
    }

    @Test
    void detachTaskTag() throws Exception{
        Long projectId = 1L;
        Long taskId = 10L;
        Long tagId = 100L;

        mockMvc.perform(delete("/projects/{project-id}/tasks/{task-id}/tags/{tag-id}",
                               projectId, taskId, tagId))
                .andExpect(status().isNoContent());

        then(service).should().detachTag(projectId, taskId, tagId);
    }
}