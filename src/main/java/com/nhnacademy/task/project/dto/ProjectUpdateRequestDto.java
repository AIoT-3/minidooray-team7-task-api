package com.nhnacademy.task.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectUpdateRequestDto {
    @NotBlank(message = "project name must be not Blank")
    @Size(max = 45, message = "project name's length must be less than or equal to 45")
    private String name;

    @Pattern(regexp = "^(?i)(ACTIVE|DORMANT|CLOSED)$", message = "project status value must be \'ACTIVE\' or \'DORMANT\' or \'CLOSED\'")

    private String status;
}
