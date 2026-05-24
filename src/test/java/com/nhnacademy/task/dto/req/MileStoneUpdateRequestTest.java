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

class MileStoneUpdateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidMileStoneUpdateRequest() {
        MileStoneUpdateRequest request = new MileStoneUpdateRequest("Valid Name", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testNameIsBlank() {
        MileStoneUpdateRequest request = new MileStoneUpdateRequest(" ", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void testNameIsTooLong() {
        String longName = "a".repeat(51);
        MileStoneUpdateRequest request = new MileStoneUpdateRequest(longName, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("length must be between 0 and 50");
    }

    @Test
    void testStartDateIsNull() {
        MileStoneUpdateRequest request = new MileStoneUpdateRequest("Valid Name", null, LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<MileStoneUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }

    @Test
    void testEndDateIsNull() {
        MileStoneUpdateRequest request = new MileStoneUpdateRequest("Valid Name", LocalDateTime.now(), null);
        Set<ConstraintViolation<MileStoneUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
}
