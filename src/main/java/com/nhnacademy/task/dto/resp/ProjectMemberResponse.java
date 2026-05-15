package com.nhnacademy.task.dto.resp;

public record ProjectMemberResponse(
        long id,
        long userId,
        String role,
        boolean isDeleted
) {}
