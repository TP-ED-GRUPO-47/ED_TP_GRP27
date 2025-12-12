package exceptions;

/**
 * Custom exception to signal that an expected element is missing.
 * Mirrors the semantics of the standard NoSuchElementException while
 * keeping dependencies limited to the project's own data structures.
 */
public class NoSuchElementException extends RuntimeException {

    /**
     * Creates a new exception with no detail message.
     */
    public NoSuchElementException() {
        super();
    }

    /**
     * Creates a new exception with the provided detail message.
     *
     * @param message explanation of the missing element scenario
     */
    public NoSuchElementException(String message) {
        super(message);
    }
}
