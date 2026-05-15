package com.nhnacademy.task.tag;

import com.nhnacademy.task.task.TaskEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * TaskTag
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskTagEntity {
    @Id
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tag_id")
    private TagEntity tagEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private TaskEntity taskEntity;
}
