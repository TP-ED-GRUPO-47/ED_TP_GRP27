package model;

/**
 * Represents an effect that can be applied to a player.
 * <p>
 * These effects are triggered by items found in the maze or by random events
 * occurring in corridors. They can modify the player's power/health or cause
 * state changes like position swapping or skipping turns.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public enum Effect {
    /** Restores a significant amount of power/health to the player. */
    HEAL(20),

    /** Inflicts damage to the player, reducing their power. */
    DAMAGE(25),

    /** Grants a small power boost to the player. */
    BONUS_POWER(15),

    /** A trap that causes a large reduction in power (negative value). */
    TRAP(-30),

    /**
     * Special effect: Forces the player to swap positions with another random player.
     * Value is 0 because the effect is logical, not numerical.
     */
    SWAP_POSITION(0),

    /**
     * Special effect: Forces the player to lose their next turn.
     * Value is 0 because the effect is logical, not numerical.
     */
    SKIP_TURN(0);

    /** The numerical magnitude of the effect (positive for gain, negative for loss). */
    private final int value;

    /**
     * Constructs an Effect with a specific numerical value.
     *
     * @param value The magnitude of the effect.
     */
    Effect(int value) {
        this.value = value;
    }

    /**
     * Retrieves the numerical value associated with this effect.
     *
     * @return The integer value of the effect.
     */
    public int getValue() {
        return value;
    }
}