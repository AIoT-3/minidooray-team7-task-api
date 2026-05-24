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

class TaskCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTaskCreateRequest() {
        TaskCreateRequest request = new TaskCreateRequest("Valid Name", "Valid Content");
        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNameIsBlank() {
        TaskCreateRequest request = new TaskCreateRequest(" ", "Valid Content");
        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void testNameIsTooLong() {
        String longName = "a".repeat(201);
        TaskCreateRequest request = new TaskCreateRequest(longName, "Valid Content");
        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("length must be between 0 and 200");
    }

    @Test
    void testContentIsBlank() {
        TaskCreateRequest request = new TaskCreateRequest("Valid Name", " ");
        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void testContentIsTooLong() {
        String longContent = "a".repeat(2001);
        TaskCreateRequest request = new TaskCreateRequest("Valid Name", longContent);
        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("length must be between 0 and 2000");
    }
}
