package com.nhnacademy.task.common;

public record ErrorResponse(
        int status,
        String message
) {}
