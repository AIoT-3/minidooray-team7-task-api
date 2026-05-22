package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectState;
import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TagServiceTest
 *
 * @author chosun-nhn12
 * @since 26. 5. 21.
 */
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    void TagSuccessfullyCreated() {
        Long projectId = 10L;
        String name = "testTag";

        ProjectEntity projectEntity = new ProjectEntity("projectTest", ProjectState.ACTIVE);
        TagEntity savedTag = new TagEntity(
                1L,
                projectEntity,
                name,
                LocalDateTime.now(),
                new ArrayList<>()
        );


        given(projectRepository.findById(projectId))
                .willReturn(Optional.of(projectEntity));

        given(tagRepository.existsByProject_idAndName(projectId, name))
                .willReturn(false);

        given(tagRepository.save(any(TagEntity.class)))
                .willReturn(savedTag);

        TagResponse response = tagService.createTag(projectId, name);

        then(projectRepository).should().findById(projectId);
        then(tagRepository).should().existsByProject_idAndName(projectId, name);
        then(tagRepository).should().save(any(TagEntity.class));

        assertEquals(1L, response.id());
        assertEquals(name, response.name());
    }


}