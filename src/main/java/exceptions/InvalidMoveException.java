package exceptions;

/**
 * Exception thrown when a player attempts an invalid movement in the maze.
 * <p>
 * This exception signals that a move is not allowed, such as trying to move
 * to a room that is not connected to the current room or is blocked.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class InvalidMoveException extends RuntimeException {
    /**
     * Constructs a new InvalidMoveException with a descriptive message.
     *
     * @param message Details about why the move is invalid.
     */
    public InvalidMoveException(String message) {
        super(message);
    }
}
