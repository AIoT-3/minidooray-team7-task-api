<<<<<<<< HEAD:src/main/java/com/nhnacademy/task/dto/req/TagCreateRequest.java
package com.nhnacademy.task.dto.req;
========
package com.nhnacademy.task.tag.dto.req;
>>>>>>>> origin/task-upper:src/main/java/com/nhnacademy/task/tag/dto/req/TagCreateRequest.java

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record TagCreateRequest(
        @NotBlank
        @Length(max = 50)
        String name
) {}
