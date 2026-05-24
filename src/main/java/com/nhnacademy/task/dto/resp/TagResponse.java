package com.nhnacademy.task.dto.resp;

import com.nhnacademy.task.entity.MilestoneEntity;
import com.nhnacademy.task.entity.TagEntity;
import com.nhnacademy.task.entity.TaskTagEntity;

public record TagResponse(
        long id,
        String name
) {
    public static TagResponse from(TagEntity tagEntity) {
        return new TagResponse(
                tagEntity.getId(),
                tagEntity.getName()
        );
    }

    public static TagResponse from(TaskTagEntity taskTagEntity){
        return new TagResponse(
                taskTagEntity.getTag().getId(),
                taskTagEntity.getTag().getName()
        );
    }
}
