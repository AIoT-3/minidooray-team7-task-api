package com.nhnacademy.task.task.dto.req;

import java.util.List;

public record TaskTagCreateRequest(
        List<Long> tagIds
) {}
