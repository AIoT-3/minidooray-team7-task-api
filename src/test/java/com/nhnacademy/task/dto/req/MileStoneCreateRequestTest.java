package com.nhnacademy.task.dto.req;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MileStoneCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidMileStoneCreateRequest() {
        MileStoneCreateRequest request = new MileStoneCreateRequest("Valid Name", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNameIsBlank() {
        MileStoneCreateRequest request = new MileStoneCreateRequest(" ", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void testNameIsTooLong() {
        String longName = "a".repeat(51);
        MileStoneCreateRequest request = new MileStoneCreateRequest(longName, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("length must be between 0 and 50");
    }

    @Test
    void testStartDateIsNull() {
        MileStoneCreateRequest request = new MileStoneCreateRequest("Valid Name", null, LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }

    @Test
    void testEndDateIsNull() {
        MileStoneCreateRequest request = new MileStoneCreateRequest("Valid Name", LocalDateTime.now(), null);
        Set<ConstraintViolation<MileStoneCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
}
