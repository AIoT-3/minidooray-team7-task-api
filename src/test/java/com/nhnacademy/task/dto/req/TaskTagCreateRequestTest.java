package com.nhnacademy.task.dto.req;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTagCreateRequestTest {

    @Test
    void testTaskTagCreateRequest() {
        List<Long> tagIds = List.of(1L, 2L, 3L);
        TaskTagCreateRequest request = new TaskTagCreateRequest(tagIds);
        assertThat(request.tagIds()).isEqualTo(tagIds);
    }
}
