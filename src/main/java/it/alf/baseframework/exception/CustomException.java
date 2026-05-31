package it.alf.baseframework.exception;

/**
 * Generic application-level exception mapped to HTTP 400 (Bad Request) by
 * {@link GlobalExceptionHandler}. Use it for business-rule violations that are the caller's
 * fault; for missing resources prefer {@link ResourceNotFoundException}.
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomException(String message) {
        super(message);
    }
}
