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

class CommentCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCommentCreateRequest() {
        CommentCreateRequest request = new CommentCreateRequest("Valid Content");
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void testContentIsBlank() {
        CommentCreateRequest request = new CommentCreateRequest(" ");
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void testContentIsTooLong() {
        String longContent = "a".repeat(46);
        CommentCreateRequest request = new CommentCreateRequest(longContent);
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("length must be between 0 and 45");
    }
}
