package model;

import java.util.Random;

/**
 * Represents a random event that occurs when traversing a corridor.
 * <p>
 * These events introduce unpredictability to the game by providing opportunities
 * to find items or imposing immediate effects (positive or negative) on the player.
 * It fulfills the requirement: "Corridors: paths... where random events may occur."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class RandomEvent {
    private final String description;
    private final Item item;
    private final Effect directEffect;
    private final Random random = new Random();

    /**
     * Constructs a new RandomEvent.
     *
     * @param description  The text description of the event.
     * @param item         An item that might be found (can be null).
     * @param directEffect An immediate effect applied to the player (can be null).
     */
    public RandomEvent(String description, Item item, Effect directEffect) {
        this.description = description;
        this.item = item;
        this.directEffect = directEffect;
    }

    /**
     * Triggers the visual aspect of the event.
     * <p>
     * Displays the description to the user. There is a 50% chance that the item
     * (if defined) will be revealed/found. The direct effect is always displayed.
     * </p>
     */
    public void trigger() {
        System.out.println("EVENTO: " + description);
        // 50% chance to find the item
        if (random.nextBoolean() && item != null) {
            System.out.println("Encontraste: " + item);
        }
        if (directEffect != null) {
            System.out.println("Efeito: " + directEffect);
        }
    }

    /**
     * Retrieves the direct effect associated with this event.
     *
     * @return The {@link Effect} to be applied to the player.
     */
    public Effect getDirectEffect() {
        return directEffect;
    }

    /**
     * Retrieves the item associated with this event.
     *
     * @return The {@link Item} object.
     */
    public Item getItem() {
        return item;
    }
}