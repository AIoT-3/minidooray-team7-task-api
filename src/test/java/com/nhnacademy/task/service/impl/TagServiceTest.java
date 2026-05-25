package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityAlreadyExistsException;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

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

    @Test
    void TagAlreadyExists() {
        Long projectId = 10L;
        String name = "testTag";

        ProjectEntity projectEntity = new ProjectEntity("projectTest", ProjectState.ACTIVE);

        given(projectRepository.findById(projectId)).willReturn(Optional.of(projectEntity));
        given(tagRepository.existsByProject_idAndName(projectId, name)).willReturn(true);

        then(tagRepository).should(never()).save(any(TagEntity.class));
        assertThrows(EntityAlreadyExistsException.class, () -> tagService.createTag(projectId, name));
    }

    @Test
    void getTagsByProjectId() {
        Long projectId = 10L;
        Long tagId = 1L;
        String tagName = "dd";

        given(projectRepository.findById(projectId))
                .willReturn(Optional.of(new ProjectEntity("projectTest", ProjectState.ACTIVE)));

        TagResponse response = new TagResponse(tagId, tagName);

        given(tagRepository.findAllByProject_Id(projectId))
                .willReturn(List.of(response));

        List<TagResponse> tagResponses = tagService.getTagsByProjectId(projectId);

        then(projectRepository).should().findById(projectId);
        then(tagRepository).should().findAllByProject_Id(projectId);

        assertEquals(tagId, tagResponses.getFirst().id());
        assertEquals(tagName, tagResponses.getFirst().name());
    }

    @Test
    void updateTagSuccessfully() {
        Long projectId = 10L;
        Long tagId = 1L;
        String name = "test";
        String updatedName = "updated";

        TagEntity tagEntity = new TagEntity(
                tagId,
                new ProjectEntity("testProject", ProjectState.ACTIVE),
                name,
                LocalDateTime.now(),
                new ArrayList<>()
        );

        given(tagRepository.findByIdAndProject_Id(tagId, projectId))
                .willReturn(Optional.of(tagEntity));

        given(tagRepository.existsByProject_idAndName(projectId, updatedName))
                .willReturn(false);

        TagResponse updatedTag = tagService.updateTag(projectId, tagId, updatedName);


        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(tagRepository).should().existsByProject_idAndName(projectId, updatedName);
        assertEquals(updatedName, updatedTag.name());
    }

    @Test
    void deleteTag() {
        Long projectId = 10L;
        Long tagId = 1L;

        ProjectEntity project = new ProjectEntity("Test", ProjectState.ACTIVE);
        TagEntity tag = new TagEntity(project, "testTag");

        given(projectRepository.findById(projectId))
                .willReturn(Optional.of(project));

        given(tagRepository.findByIdAndProject_Id(tagId, projectId))
                .willReturn(Optional.of(tag));

        tagService.deleteTag(projectId, tagId);

        then(projectRepository).should().findById(projectId);
        then(tagRepository).should().findByIdAndProject_Id(tagId, projectId);
        then(tagRepository).should().delete(tag);
    }


}