package it.alf.baseframework.exception;

/**
 * Thrown when a requested resource does not exist. Mapped to HTTP 404 by
 * {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Convenience factory building a consistent message for a missing entity.
     *
     * @param resource the resource/entity type name (e.g. {@code "MyEntity"})
     * @param id       the identifier that was not found
     */
    public static ResourceNotFoundException of(String resource, Object id) {
        return new ResourceNotFoundException(resource + " with id '" + id + "' was not found");
    }
}
