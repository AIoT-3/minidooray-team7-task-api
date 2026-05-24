package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.entity.TaskTagEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TagResponseTest {

    @Test
    void testFromTagEntity() {
        TagEntity tagEntity = Mockito.mock(TagEntity.class);
        when(tagEntity.getId()).thenReturn(1L);
        when(tagEntity.getName()).thenReturn("Test Tag");

        TagResponse response = TagResponse.from(tagEntity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Test Tag");
    }

    @Test
    void testFromTaskTagEntity() {
        TaskTagEntity taskTagEntity = Mockito.mock(TaskTagEntity.class);
        TagEntity tagEntity = Mockito.mock(TagEntity.class);
        
        when(tagEntity.getId()).thenReturn(2L);
        when(tagEntity.getName()).thenReturn("Task Tag");
        when(taskTagEntity.getTag()).thenReturn(tagEntity);

        TagResponse response = TagResponse.from(taskTagEntity);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Task Tag");
    }
}
