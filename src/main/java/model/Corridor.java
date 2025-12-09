package model;

/**
 * Represents a corridor that connects two rooms in the maze.
 * <p>
 * A corridor acts as a weighted edge in the graph structure (NetworkList).
 * It has a travel cost (weight) and can optionally contain a {@link RandomEvent}
 * that triggers when a player traverses it.
 * </p>
 * <p>
 * It fulfills the requirement: "Corridors: paths connecting divisions, where random events may occur."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Corridor {

    private final Room source;
    private final Room target;
    private final double weight;
    private final RandomEvent event;

    /**
     * Constructs a corridor with a specific weight and an optional random event.
     *
     * @param source The starting room of the corridor.
     * @param target The destination room of the corridor.
     * @param weight The cost or weight associated with traversing this corridor.
     * @param event  A {@link RandomEvent} associated with this corridor (can be null).
     */
    public Corridor(Room source, Room target, double weight, RandomEvent event) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.event = event;
    }

    /**
     * Gets the source room of this corridor.
     *
     * @return The starting {@link Room}.
     */
    public Room getSource() { return source; }

    /**
     * Gets the target room of this corridor.
     *
     * @return The destination {@link Room}.
     */
    public Room getTarget() { return target; }

    /**
     * Gets the weight (cost) of traversing this corridor.
     *
     * @return The weight as a double.
     */
    public double getWeight() { return weight; }

    /**
     * Gets the random event associated with this corridor.
     *
     * @return The {@link RandomEvent} object, or null if none exists.
     */
    public RandomEvent getEvent() { return event; }

    /**
     * Triggers the random event associated with this corridor, if one exists.
     * <p>
     * This method is typically called by the game engine when a player moves through this corridor.
     * </p>
     */
    public void triggerEvent() {
        if (event != null) {
            event.trigger();
        }
    }

    /**
     * Returns a string representation of the corridor.
     *
     * @return A string describing the connection, weight, and presence of an event.
     */
    @Override
    public String toString() {
        return source.getId() + " --(" + weight + ")--> " + target.getId() +
                (event != null ? " [EVENT]" : "");
    }
}