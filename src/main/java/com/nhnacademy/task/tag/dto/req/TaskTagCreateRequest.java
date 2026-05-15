package com.nhnacademy.task.tag.dto.req;

import java.util.List;

public record TaskTagCreateRequest(
        List<Long> tagIds
) {}
