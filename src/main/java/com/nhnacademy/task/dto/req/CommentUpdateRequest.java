<<<<<<<< HEAD:src/main/java/com/nhnacademy/task/dto/req/CommentUpdateRequest.java
package com.nhnacademy.task.dto.req;
========
package com.nhnacademy.task.comment.dto.req;
>>>>>>>> origin/task-upper:src/main/java/com/nhnacademy/task/comment/dto/req/CommentUpdateRequest.java

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CommentUpdateRequest(
        @Length(max = 45)
        @NotBlank
        String content
) {}
