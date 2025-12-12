package model;

import java.util.Random;

/**
 * Represents a random event that occurs when traversing a corridor.
 * <p>
 * These events introduce unpredictability to the game by imposing immediate effects
 * (positive or negative) on the player.
 * It fulfills the requirement: "Corridors: paths... where random events may occur."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class RandomEvent {
    private final String description;
    private final Effect directEffect;
    private final Random random = new Random();

    /**
     * Constructs a new RandomEvent.
     *
     * @param description  The text description of the event.
     * @param directEffect An immediate effect applied to the player (can be null).
     */
    public RandomEvent(String description, Effect directEffect) {
        this.description = description;
        this.directEffect = directEffect;
    }

    /**
     * Triggers the visual aspect of the event.
     * <p>
     * Displays the description to the user. The direct effect is always displayed.
     * </p>
     *
     * @param player player affected by this event
     */
    public void trigger(Player player) {
        System.out.println("Evento: " + description);
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
     * Retrieves the description of this event.
     *
     * @return textual description of the event
     */
    public String getDescription() {
        return description;
    }
}