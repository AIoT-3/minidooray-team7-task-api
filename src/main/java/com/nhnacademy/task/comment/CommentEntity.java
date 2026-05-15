package com.nhnacademy.task.comment;

import com.nhnacademy.task.project_member.ProjectMemberEntity;
import com.nhnacademy.task.task.TaskEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Comments
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private TaskEntity taskEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_member_id")
    private ProjectMemberEntity projectMemberEntity;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
