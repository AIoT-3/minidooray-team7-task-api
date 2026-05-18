<<<<<<<< HEAD:src/main/java/com/nhnacademy/task/dto/req/CommentCreateRequest.java
package com.nhnacademy.task.dto.req;
========
package com.nhnacademy.task.comment.dto.req;
>>>>>>>> origin/task-upper:src/main/java/com/nhnacademy/task/comment/dto/req/CommentCreateRequest.java

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CommentCreateRequest(
        @Length(max = 45)
        @NotBlank
        String content
) {}
