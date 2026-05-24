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

class CommentUpdateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCommentUpdateRequest() {
        CommentUpdateRequest request = new CommentUpdateRequest("Valid Content");
        Set<ConstraintViolation<CommentUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testContentIsBlank() {
        CommentUpdateRequest request = new CommentUpdateRequest(" ");
        Set<ConstraintViolation<CommentUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void testContentIsTooLong() {
        String longContent = "a".repeat(46);
        CommentUpdateRequest request = new CommentUpdateRequest(longContent);
        Set<ConstraintViolation<CommentUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("length must be between 0 and 45");
    }
}
