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
     * Displays a welcome message to the console, signaling that the player
     * is at the starting point of the adventure.
     * </p>
     */
    @Override
    public void onEnter() {
        System.out.println("Estás na entrada: " + getDescription());
        System.out.println("A aventura começa aqui!");
    }
}