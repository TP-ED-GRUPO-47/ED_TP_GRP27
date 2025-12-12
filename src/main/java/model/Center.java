package model;

/**
 * Represents the Center room (Treasure Room) of the maze.
 * <p>
 * This is a special type of {@link Room} that serves as the ultimate goal of the game.
 * According to the game rules, the first player to reach this room wins the match.
 * </p>
 * <p>
 * It fulfills the requirement: "A central point - the room where the treasure is found."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Center extends Room {

    /**
     * Constructs a new Center room.
     *
     * @param id          The unique identifier of the room.
     * @param description The text description of the room (e.g., "The Throne Room").
     */
    public Center(String id, String description) {
        super(id, description);
    }

    /**
     * Executed when a player enters this room.
     * <p>
     * In the case of the Center room, this method triggers the victory message,
     * indicating that the player has found the treasure.
     * </p>
     */
    @Override
    public void onEnter() {
        System.out.println("Encontraste o tesouro: " + getDescription());
    }
}