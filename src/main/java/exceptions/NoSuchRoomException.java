package exceptions;

/**
 * Exception thrown when attempting to access a room that doesn't exist in the maze.
 * <p>
 * This exception is used when a room ID is referenced but the corresponding
 * room cannot be found in the maze structure.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class NoSuchRoomException extends RuntimeException {
    /**
     * Constructs a new NoSuchRoomException with a descriptive message.
     *
     * @param message Details about the room that was not found.
     */
    public NoSuchRoomException(String message) {
        super(message);
    }
}
