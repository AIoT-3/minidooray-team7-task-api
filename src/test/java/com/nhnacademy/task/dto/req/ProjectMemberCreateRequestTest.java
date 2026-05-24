package com.nhnacademy.task.dto.req;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectMemberCreateRequestTest {

    @Test
    void testProjectMemberCreateRequest() {
        long userId = 1L;
        ProjectMemberCreateRequest request = new ProjectMemberCreateRequest(userId);
        assertThat(request.userId()).isEqualTo(userId);
    }
}
