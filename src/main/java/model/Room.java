package model;

/**
 * Abstract class representing a generic room in the maze.
 * <p>
 * All rooms possess a unique ID and a description.
 * The {@code onEnter()} method is automatically called when a player enters the room.
 * {@code equals()} and {@code hashCode()} are based on the ID to ensure correct behavior
 * in data structures such as graphs or maps.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public abstract class Room {

    /** Unique identifier for the room */
    private final String id;

    /** Textual description of the room */
    private final String description;

    /**
     * Constructs a new Room.
     *
     * @param id          Unique identifier for the room (e.g., "E1", "R1").
     * @param description Description visible to the player.
     */
    public Room(String id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Abstract method called automatically when a player enters the room.
     * Each specific room type (Entrance, Riddle, Treasure, etc.) must define its own behavior.
     */
    public abstract void onEnter();

    /**
     * Retrieves the unique identifier of the room.
     *
     * @return The room ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the description of the room.
     *
     * @return The text description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if this room is equal to another object based on the ID.
     *
     * @param o The object to compare.
     * @return true if the IDs match, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        if (id == null && room.id == null) {
            return true;
        }
        if (id == null || room.id == null) {
            return false;
        }
        return id.equals(room.id);
    }

    /**
     * Generates a hash code for the room based on its ID.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * Returns a string representation of the room.
     *
     * @return A string in the format "[ID: Description]".
     */
    @Override
    public String toString() {
        return "[" + id + ": " + description + "]";
    }
}