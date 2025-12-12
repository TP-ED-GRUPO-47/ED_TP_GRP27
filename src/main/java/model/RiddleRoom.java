package model;

/**
 * Represents a room containing a riddle or logic puzzle.
 * <p>
 * This type of room challenges the player with a question loaded from an external file.
 * To proceed or unlock the path, the player must answer correctly.
 * It fulfills the requirement regarding "Divisions with riddles".
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class RiddleRoom extends Room {
    private Riddle riddle;
    private boolean isSolved;

    /**
     * Constructs a new RiddleRoom.
     *
     * @param id          The unique identifier of the room.
     * @param description The text description of the room.
     * @param riddle      The {@link Riddle} object associated with this room.
     */
    public RiddleRoom(String id, String description, Riddle riddle) {
        super(id, description);
        this.riddle = riddle;
        this.isSolved = false;
    }

    /**
     * Retrieves the riddle associated with this room.
     *
     * @return The {@link Riddle} object.
     */
    public Riddle getRiddle() { return riddle; }

    /**
     * Checks if the riddle in this room has been solved.
     *
     * @return true if solved, false otherwise.
     */
    public boolean isSolved() { return isSolved; }

    /**
     * Sets the status of the riddle (solved or unsolved).
     *
     * @param solved true to mark as solved, false otherwise.
     */
    public void setSolved(boolean solved) { this.isSolved = solved; }

    /**
     * Executed when a player enters the room.
     * <p>
     * Displays a message indicating that this is a riddle room.
     * If solved, it informs the player they can pass; otherwise, it warns
     * that an answer is required.
     * </p>
     */
    @Override
    public void onEnter() {
        System.out.println("\nEnigma: " + getDescription());
        if (isSolved) {
            System.out.println("O guardião deixa-te passar.");
        } else {
            System.out.println("Tens de responder para passar!\n");
        }
    }
}