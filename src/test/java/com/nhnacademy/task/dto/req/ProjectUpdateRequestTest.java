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

class ProjectUpdateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidProjectUpdateRequest() {
        ProjectUpdateRequest request = new ProjectUpdateRequest("Valid Name", "ACTIVE");
        Set<ConstraintViolation<ProjectUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNameIsBlank() {
        ProjectUpdateRequest request = new ProjectUpdateRequest("", "ACTIVE");
        Set<ConstraintViolation<ProjectUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("project name must be not Blank");
    }

    @Test
    void testNameIsTooLong() {
        String longName = "a".repeat(46);
        ProjectUpdateRequest request = new ProjectUpdateRequest(longName, "ACTIVE");
        Set<ConstraintViolation<ProjectUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("project name's length must be less than or equal to 45");
    }

    @Test
    void testInvalidState() {
        ProjectUpdateRequest request = new ProjectUpdateRequest("Valid Name", "INVALID_STATE");
        Set<ConstraintViolation<ProjectUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("project status value must be 'ACTIVE' or 'DORMANT' or 'CLOSED'");
    }

    @Test
    void testLowerCaseState() {
        ProjectUpdateRequest request = new ProjectUpdateRequest("Valid Name", "active");
        Set<ConstraintViolation<ProjectUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
