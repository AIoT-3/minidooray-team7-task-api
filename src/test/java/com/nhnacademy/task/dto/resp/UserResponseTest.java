package com.nhnacademy.task.dto.resp;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    void testUserResponse() {
        Long id = 1L;
        String userId = "testUser";
        String email = "test@example.com";
        UserStatus status = UserStatus.ACTIVATE;
        LocalDateTime createdAt = LocalDateTime.now();

        UserResponse response = new UserResponse(id, userId, email, status, createdAt);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.status()).isEqualTo(status);
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }
}
