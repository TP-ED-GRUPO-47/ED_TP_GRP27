package exceptions;

/**
 * Exception thrown when JSON parsing or validation fails.
 * <p>
 * This exception is used when loading maps or riddles from JSON files
 * and the structure or content is invalid or malformed.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class InvalidJsonException extends RuntimeException {
    /**
     * Constructs a new InvalidJsonException with a descriptive message.
     *
     * @param message Details about the JSON validation error.
     */
    public InvalidJsonException(String message) {
        super(message);
    }
}
