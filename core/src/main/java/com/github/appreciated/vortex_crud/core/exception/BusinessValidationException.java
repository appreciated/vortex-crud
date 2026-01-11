package com.github.appreciated.vortex_crud.core.exception;

/**
 * Exception used to indicate a business validation failure that should be displayed to the user.
 */
public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }
}
