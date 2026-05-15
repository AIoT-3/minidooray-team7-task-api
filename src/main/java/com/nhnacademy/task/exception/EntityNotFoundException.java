package com.nhnacademy.task.exception;

/**
 * EntityAlreadyException
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
