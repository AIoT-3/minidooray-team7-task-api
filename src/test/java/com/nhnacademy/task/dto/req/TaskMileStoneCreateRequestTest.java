package com.nhnacademy.task.dto.req;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMileStoneCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTaskMileStoneCreateRequest() {
        // primitive type (long) cannot be null, so it will always be valid
        TaskMileStoneCreateRequest request = new TaskMileStoneCreateRequest(1L);
        Set<ConstraintViolation<TaskMileStoneCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
