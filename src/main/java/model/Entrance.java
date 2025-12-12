package model;

/**
 * Represents the Entrance room of the maze.
 * <p>
 * This class extends {@link Room} and defines the starting point of the game.
 * [cite_start]It fulfills the requirement: "Various entry points - possible starting locations for players." [cite: 22]
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Entrance extends Room {

    /**
     * Constructs a new Entrance room.
     *
     * @param id          The unique identifier of the room (e.g., "E1").
     * @param description A brief description of the entrance surroundings.
     */
    public Entrance(String id, String description) {
        super(id, description);
    }

    /**
     * Executed when a player enters this room.
     * <p>
     * Displays the entrance description. The welcome message "A tua aventura começa aqui!"
     * is shown during game initialization in GameEngine, not here, to avoid repetition
     * when players revisit entrance rooms during gameplay.
     * </p>
     */
    @Override
    public void onEnter() {
        System.out.println("\nEstás na entrada: " + getDescription());
    }
}