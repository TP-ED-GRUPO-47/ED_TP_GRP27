package exceptions;

/**
 * Exception thrown when a requested element cannot be found in a collection.
 * <p>
 * This exception extends RuntimeException and is used when searching for elements
 * in data structures that don't contain the specified element.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class ElementNotFoundException extends RuntimeException {
    /**
     * Constructs a new ElementNotFoundException with a descriptive message.
     *
     * @param message Details about the element that was not found.
     */
    public ElementNotFoundException(String message) {
        super(message);
    }
}
