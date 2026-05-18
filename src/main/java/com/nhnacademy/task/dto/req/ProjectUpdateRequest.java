package com.nhnacademy.task.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProjectUpdateRequest (
    @NotBlank(message = "project name must be not Blank")
    @Size(max = 45, message = "project name's length must be less than or equal to 45")
    String name,

    @Pattern(regexp = "^(?i)(ACTIVE|DORMANT|CLOSED)$", message = "project status value must be \'ACTIVE\' or \'DORMANT\' or \'CLOSED\'")
    String status
){}
