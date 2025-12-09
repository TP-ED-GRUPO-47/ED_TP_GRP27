package model;

/**
 * Represents a standard room in the maze with no special events or mechanics.
 * <p>
 * This class serves as a basic implementation of {@link Room}, functioning primarily
 * as a connector or a simple location within the game map.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class RoomStandard extends Room {

    /**
     * Constructs a new Standard Room.
     *
     * @param id          The unique identifier of the room.
     * @param description The text description of the room.
     */
    public RoomStandard(String id, String description) {
        super(id, description);
    }

    /**
     * Executed when a player enters this room.
     * <p>
     * Displays a standard message indicating the player has entered a normal room,
     * along with its description.
     * </p>
     */
    @Override
    public void onEnter() {
        System.out.println("Entraste numa sala normal: " + getDescription());
    }
}