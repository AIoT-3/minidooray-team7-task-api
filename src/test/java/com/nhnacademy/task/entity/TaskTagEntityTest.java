package com.nhnacademy.task.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TaskTagEntityTest {

    @Mock
    private TagEntity mockTag;

    @Mock
    private TaskEntity mockTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockTag.getTaskTagList()).thenReturn(new ArrayList<>());
        when(mockTask.getTaskTagList()).thenReturn(new ArrayList<>());
    }

    @Test
    void testTaskTagEntityCreation() {
        TaskTagEntity taskTag = new TaskTagEntity(mockTag, mockTask);

        assertThat(taskTag.getTag()).isEqualTo(mockTag);
        assertThat(taskTag.getTask()).isEqualTo(mockTask);
    }

    @Test
    void testSetTag() {
        TaskTagEntity taskTag = new TaskTagEntity(null, mockTask);

        taskTag.setTag(mockTag);

        assertThat(taskTag.getTag()).isEqualTo(mockTag);
        assertThat(mockTag.getTaskTagList()).contains(taskTag);

        taskTag.setTag(null);
        assertThat(taskTag.getTag()).isNull();
    }

    @Test
    void testSetTask() {
        TaskTagEntity taskTag = new TaskTagEntity(mockTag, null);

        taskTag.setTask(mockTask);

        assertThat(taskTag.getTask()).isEqualTo(mockTask);
        assertThat(mockTask.getTaskTagList()).contains(taskTag);

        taskTag.setTask(null);
        assertThat(taskTag.getTask()).isNull();
    }
}
