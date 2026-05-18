<<<<<<<< HEAD:src/main/java/com/nhnacademy/task/dto/req/TagUpdateRequest.java
package com.nhnacademy.task.dto.req;
========
package com.nhnacademy.task.tag.dto.req;
>>>>>>>> origin/task-upper:src/main/java/com/nhnacademy/task/tag/dto/req/TagUpdateRequest.java

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TagUpdateRequest(
        @NotBlank
        @Length(max = 50)
        String name
) {}
