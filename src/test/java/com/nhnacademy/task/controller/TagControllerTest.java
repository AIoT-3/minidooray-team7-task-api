package com.nhnacademy.task.controller;

import com.nhnacademy.task.dto.req.TagCreateRequest;
import com.nhnacademy.task.dto.req.TagUpdateRequest;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.service.TagService;
import org.junit.jupiter.api.DisplayName;
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
 * TagControllerTest
 *
 * @author chosun-nhn12
 * @since 26. 5. 21.
 */
@WebMvcTest(TagController.class)
class TagControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TagService tagService;


    @Test
    @DisplayName("태그목록 조회")
    void getTags() throws Exception{
        Long projectId = 1L;

        TagResponse response = new TagResponse(
                100L,
                "태그1"
        );

        when(tagService.getTagsByProjectId(projectId))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/projects/{project-id}/tags", projectId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("태그1"));

        then(tagService).should().getTagsByProjectId(projectId);
    }

    @Test
    @DisplayName("태그 생성")
    void createTag() throws Exception {
        Long projectId = 1L;
        String name = "태그1";

        TagResponse response = new TagResponse(
                100L,
                "태그1"
        );

        when(tagService.createTag(projectId, name))
                .thenReturn(response);

        TagCreateRequest request = new TagCreateRequest(name);

        mockMvc.perform(
                    post("/projects/{project-id}/tags", projectId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value(name));

        then(tagService).should().createTag(projectId, name);
    }

    @Test
    @DisplayName("태그 수정")
    void updateTag() throws Exception {
        Long projectId = 1L;
        Long tagId = 10L;
        String name = "테스트1";
        TagUpdateRequest request = new TagUpdateRequest(name);

        TagResponse response = new TagResponse(
                tagId,
                name
        );

        when(tagService.updateTag(projectId, tagId, name))
                .thenReturn(response);

        mockMvc.perform(put("/projects/{project-id}/tags/{tag-id}", projectId, tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tagId))
                .andExpect(jsonPath("$.name").value(name));

        then(tagService).should().updateTag(projectId, tagId, request.name());

    }

    @Test
    @DisplayName("태그 삭제")
    void deleteTag() throws Exception {
        Long projectId = 1L;
        Long tagId = 10L;

        mockMvc.perform(delete("/projects/{project-id}/tags/{tag-id}", projectId, tagId))
                .andExpect(status().isNoContent());

        then(tagService).should().deleteTag(projectId, tagId);
    }

    @Test
    void createTag_invalidBlankName() throws Exception {
        Long projectId = 1L;
        TagCreateRequest request = new TagCreateRequest("");

        mockMvc.perform(post("/projects/{project-id}/tags", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }
}