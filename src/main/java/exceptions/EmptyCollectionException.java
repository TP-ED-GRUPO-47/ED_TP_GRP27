package exceptions;

/**
 * Exception thrown when attempting to access or remove an element from an empty collection.
 * <p>
 * This exception extends RuntimeException and is used throughout the custom data structures
 * to signal operations that cannot be performed on empty collections (e.g., pop from empty stack).
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class EmptyCollectionException extends RuntimeException {
    /**
     * Constructs a new EmptyCollectionException with a specific collection name.
     *
     * @param collection The name of the collection that is empty.
     */
    public EmptyCollectionException(String collection) {
        super("The " + collection + " is empty.");
    }
}
