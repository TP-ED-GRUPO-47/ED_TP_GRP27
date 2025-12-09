package model;

import java.util.Random;

/**
 * Represents a room containing a lever that triggers unpredictable effects in the game.
 * <p>
 * [cite_start]Interacting with the lever can open secret paths[cite: 33], grant items, or activate traps.
 * This class fulfills the project requirement regarding "levers" and "unexpected effects".
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class LeverRoom extends Room {

    /**
     * Defines the possible outcomes of pulling the lever.
     */
    public enum LeverResult {
        /** The lever opens a new path or secret passage. */
        UNLOCK_PATH,
        /** The lever triggers a trap, damaging the player. */
        TRAP,
        /** The lever does nothing (a dud). */
        NOTHING
    }

    private final Random random = new Random();
    private boolean activated = false;

    /**
     * Constructs a new LeverRoom.
     *
     * @param id          The unique identifier of the room.
     * @param description The text description of the room.
     */
    public LeverRoom(String id, String description) {
        super(id, description);
    }

    /**
     * Executed when a player enters the room.
     * <p>
     * Displays the status of the lever (whether it is ready to be pulled or already jammed/used).
     * </p>
     */
    @Override
    public void onEnter() {
        System.out.println(">> [LEVER] " + getDescription());
        if (activated) {
            System.out.println("The lever is jammed in the final position.");
        } else {
            System.out.println("A mysterious lever is on the wall.");
        }
    }

    /**
     * Checks if the lever has already been activated.
     *
     * @return true if the lever was already pulled, false otherwise.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Activates the lever and generates a random effect.
     * <p>
     * Probabilities:
     * <ul>
     * [cite_start]<li>40% chance to unlock a path[cite: 33].</li>
     * <li>30% chance to trigger a trap.</li>
     * <li>30% chance of nothing happening.</li>
     * </ul>
     * </p>
     *
     * @return The result of the action (UNLOCK_PATH, TRAP, or NOTHING).
     */
    public LeverResult pullLever() {
        if (activated) return LeverResult.NOTHING;

        activated = true;
        int chance = random.nextInt(100);

        if (chance < 40) {
            return LeverResult.UNLOCK_PATH;
        } else if (chance < 70) {
            return LeverResult.TRAP;
        } else {
            return LeverResult.NOTHING;
        }
    }
}