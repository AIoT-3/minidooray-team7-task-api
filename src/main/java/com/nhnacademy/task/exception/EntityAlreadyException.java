package com.nhnacademy.task.exception;

/**
 * EntityAlreadyException
 *
 * @author chosun-nhn12
 * @since 26. 5. 15.
 */
public class EntityAlreadyException extends RuntimeException {
    public EntityAlreadyException(String message) {
        super(message);
    }
}
