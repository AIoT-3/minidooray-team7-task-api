package com.nhnacademy.task.project.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest (
    @NotBlank(message = "project name must be not Blank")
    @Size(max = 45, message = "project name's length must be less than or equal to 45")
    String name
){}
