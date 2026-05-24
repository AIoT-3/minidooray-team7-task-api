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

class ProjectCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidProjectCreateRequest() {
        ProjectCreateRequest request = new ProjectCreateRequest("Valid Project Name");
        Set<ConstraintViolation<ProjectCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNameIsBlank() {
        ProjectCreateRequest request = new ProjectCreateRequest("");
        Set<ConstraintViolation<ProjectCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("project name must be not Blank");
    }

    @Test
    void testNameIsTooLong() {
        String longName = "a".repeat(46);
        ProjectCreateRequest request = new ProjectCreateRequest(longName);
        Set<ConstraintViolation<ProjectCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("project name's length must be less than or equal to 45");
    }
}
