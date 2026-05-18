package com.nhnacademy.task.dto.req;

import java.util.List;

public record TaskTagCreateRequest(
        List<Long> tagIds
) {}
