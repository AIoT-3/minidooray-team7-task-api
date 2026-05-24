package com.nhnacademy.task.service.impl;

import com.nhnacademy.task.common.exception.EntityAlreadyExistsException;
import com.nhnacademy.task.common.exception.EntityNotFoundException;
import com.nhnacademy.task.dto.resp.TagResponse;
import com.nhnacademy.task.entity.ProjectEntity;
import com.nhnacademy.task.entity.ProjectState;
import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.repository.ProjectRepository;
import com.nhnacademy.task.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTag() {
        Long projectId = 1L;
        String tagName = "New Tag";
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        TagEntity tagEntity = mock(TagEntity.class);
        when(tagEntity.getId()).thenReturn(1L);
        when(tagEntity.getName()).thenReturn(tagName);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(tagRepository.existsByProject_idAndName(projectId, tagName)).thenReturn(false);
        when(tagRepository.save(any(TagEntity.class))).thenReturn(tagEntity);

        TagResponse response = tagService.createTag(projectId, tagName);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo(tagName);
        verify(tagRepository, times(1)).save(any(TagEntity.class));
    }

    @Test
    void testCreateTag_ProjectNotFound() {
        Long projectId = 1L;
        String tagName = "New Tag";

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.createTag(projectId, tagName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Project not found");
    }

    @Test
    void testCreateTag_TagAlreadyExists() {
        Long projectId = 1L;
        String tagName = "Existing Tag";
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(tagRepository.existsByProject_idAndName(projectId, tagName)).thenReturn(true);

        assertThatThrownBy(() -> tagService.createTag(projectId, tagName))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("tag already exists");
    }

    @Test
    void testGetTagsByProjectId() {
        Long projectId = 1L;
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        TagResponse tagResponse = new TagResponse(1L, "Tag");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(tagRepository.findAllByProject_Id(projectId)).thenReturn(List.of(tagResponse));

        List<TagResponse> responses = tagService.getTagsByProjectId(projectId);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("Tag");
    }

    @Test
    void testUpdateTag() {
        Long projectId = 1L;
        Long tagId = 1L;
        String newName = "Updated Tag";
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        TagEntity tagEntity = spy(new TagEntity(projectEntity, "Old Tag"));
        when(tagEntity.getId()).thenReturn(tagId);

        when(tagRepository.findByIdAndProject_Id(tagId, projectId)).thenReturn(Optional.of(tagEntity));
        when(tagRepository.existsByProject_idAndName(projectId, newName)).thenReturn(false);

        TagResponse response = tagService.updateTag(projectId, tagId, newName);

        assertThat(response.id()).isEqualTo(tagId);
        assertThat(response.name()).isEqualTo(newName);
        assertThat(tagEntity.getName()).isEqualTo(newName);
    }

    @Test
    void testUpdateTag_TagNotFound() {
        Long projectId = 1L;
        Long tagId = 1L;
        String newName = "Updated Tag";

        when(tagRepository.findByIdAndProject_Id(tagId, projectId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.updateTag(projectId, tagId, newName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Tag not found");
    }
    
    @Test
    void testUpdateTag_TagAlreadyExists() {
        Long projectId = 1L;
        Long tagId = 1L;
        String newName = "Updated Tag";
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        TagEntity tagEntity = new TagEntity(projectEntity, "Old Tag");

        when(tagRepository.findByIdAndProject_Id(tagId, projectId)).thenReturn(Optional.of(tagEntity));
        when(tagRepository.existsByProject_idAndName(projectId, newName)).thenReturn(true);

        assertThatThrownBy(() -> tagService.updateTag(projectId, tagId, newName))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("tag already exists");
    }

    @Test
    void testDeleteTag() {
        Long projectId = 1L;
        Long tagId = 1L;
        ProjectEntity projectEntity = new ProjectEntity("Project", ProjectState.ACTIVE);
        TagEntity tagEntity = new TagEntity(projectEntity, "Tag To Delete");
        projectEntity.addTag(tagEntity);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(tagRepository.findByIdAndProject_Id(tagId, projectId)).thenReturn(Optional.of(tagEntity));

        tagService.deleteTag(projectId, tagId);

        assertThat(projectEntity.getTagList()).doesNotContain(tagEntity);
        verify(tagRepository, times(1)).delete(tagEntity);
    }
}
