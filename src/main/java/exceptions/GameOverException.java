package exceptions;

/**
 * Exception thrown when the game ends due to a terminal condition.
 * <p>
 * This exception signals that the game has reached a terminal state,
 * such as a player reaching the treasure or losing all health.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class GameOverException extends RuntimeException {
    /**
     * Constructs a new GameOverException with a descriptive message.
     *
     * @param message Details about why the game ended.
     */
    public GameOverException(String message) {
        super(message);
    }
}
