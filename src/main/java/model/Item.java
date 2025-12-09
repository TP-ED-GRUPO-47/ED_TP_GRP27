package model;

/**
 * Represents an item that can be found during random events in the maze.
 * <p>
 * Items are typically discovered in corridors and carry specific {@link Effect}s
 * that are applied to the player (e.g., healing potions, power boosts).
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Item {
    private final String name;
    private final Effect effect;

    /**
     * Constructs a new Item with a name and a specific effect.
     *
     * @param name   The name of the item (e.g., "Health Potion").
     * @param effect The {@link Effect} associated with using or picking up this item.
     */
    public Item(String name, Effect effect) {
        this.name = name;
        this.effect = effect;
    }

    /**
     * Retrieves the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() { return name; }

    /**
     * Retrieves the effect associated with this item.
     *
     * @return The {@link Effect} enum value.
     */
    public Effect getEffect() { return effect; }

    /**
     * Returns a string representation of the item.
     *
     * @return A string in the format "Name (Effect)".
     */
    @Override
    public String toString() {
        return name + " (" + effect + ")";
    }
}