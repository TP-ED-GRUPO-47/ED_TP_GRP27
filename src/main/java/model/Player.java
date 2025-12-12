package model;

import java.util.Iterator;

import structures.linear.ArrayUnorderedList;
import structures.stack.LinkedStack;

/**
 * Represents a player in the Glory Maze.
 * <p>
 * The player has a name, a current position in the maze, a power level (health),
 * and a history of movements made during the game. It also tracks status effects
 * like skipping turns.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Player {

    private String name;
    private Room currentRoom;
    private long power;
    private boolean skipNextTurn;
    private Room lastSwappedPosition;

    /**
     * Movement history used for the final report.
     * Stores the IDs of the rooms visited.
     */
    private LinkedStack<String> movementHistory;

    /**
     * Tracks riddles solved during the game.
     */
    private ArrayUnorderedList<String> solvedRiddles;

    /**
     * Tracks effects applied to this player.
     */
    private ArrayUnorderedList<String> appliedEffects;

    /**
     * Tracks random events encountered.
     */
    private ArrayUnorderedList<String> encounteredEvents;

    /**
     * Constructs a new Player.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.power = 100; 
        this.movementHistory = new LinkedStack<>();
        this.skipNextTurn = false;
        this.lastSwappedPosition = null;
        this.solvedRiddles = new ArrayUnorderedList<>();
        this.appliedEffects = new ArrayUnorderedList<>();
        this.encounteredEvents = new ArrayUnorderedList<>();
    }

    /**
     * Sets the room where the player is currently located.
     * <p>
     * This method automatically registers the room ID in the movement history
     * and triggers the room's {@code onEnter()} logic.
     * </p>
     *
     * @param room The new current room.
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
        if (room != null) {
            this.movementHistory.push(room.getId());
            room.onEnter();
        }
    }

    /**
     * Retrieves the current room of the player.
     *
     * @return The {@link Room} where the player is located.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Retrieves the player's name.
     *
     * @return The name string.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the player's power level.
     * <p>
     * Used when applying effects like damage (negative amount) or healing (positive amount).
     * The power level cannot drop below zero.
     * </p>
     *
     * @param amount The amount to add (positive) or subtract (negative).
     */
    public void updatePower(int amount) {
        this.power += amount;
        System.out.println("Power: " + name + ": " + (power - amount) + " -> " + power);

        if (this.power < 0){
            this.power = 0;
        } 
    }

    /**
     * Retrieves the current power level of the player.
     *
     * @return The power value.
     */
    public long getPower() {
        return power;
    }

    /**
     * Checks if the player is currently under the effect of skipping a turn.
     *
     * @return true if the player must skip the next turn, false otherwise.
     */
    public boolean skipsTurn() {
        return skipNextTurn;
    }

    /**
     * Sets the flag to skip the next turn.
     *
     * @param skip true to skip the turn, false to reset.
     */
    public void setSkipNextTurn(boolean skip) {
        this.skipNextTurn = skip;
    }

    /**
     * Package-private method to silently set room without triggering onEnter.
     * Used for swaps where we don't want redundant output.
     */
    void setCurrentRoomSilent(Room room) {
        this.currentRoom = room;
        if (room != null) {
            this.movementHistory.push(room.getId());
        }
    }

    /**
     * Package-private getter for movement history (for RECEDE implementation).
     */
    LinkedStack<String> getMovementHistory() {
        return movementHistory;
    }

    /**
     * Returns a string representation of the movement history.
     * Used for generating the final JSON report.
     *
     * @return A string representing the stack of visited rooms.
     */
    public String getHistoryString() {
        return movementHistory.toString();
    }

    /**
     * Returns a string representation of the Player object.
     *
     * @return A string containing the player's name and current room ID.
     */
    @Override
    public String toString() {
        return "Player: " + name + " [Room: " + (currentRoom != null ? currentRoom.getId() : "None") + "]";
    }

    /**
     * Records that a riddle was solved.
     *
     * @param riddleId The identifier of the solved riddle.
     */
    public void recordSolvedRiddle(String riddleId) {
        solvedRiddles.addToRear(riddleId);
    }

    /**
     * Records that an effect was applied to the player.
     *
     * @param effectName The name of the effect.
     */
    public void recordAppliedEffect(String effectName) {
        appliedEffects.addToRear(effectName);
    }

    /**
     * Records that a random event was encountered.
     *
     * @param eventDescription The description of the event.
     */
    public void recordEncounteredEvent(String eventDescription) {
        encounteredEvents.addToRear(eventDescription);
    }

    /**
     * Gets the list of solved riddles.
     *
     * @return Iterator of riddle IDs.
     */
    public Iterator<String> getSolvedRiddles() {
        return copyIterator(solvedRiddles.iterator());
    }

    /**
     * Gets the list of applied effects.
     *
     * @return Iterator of effect names.
     */
    public Iterator<String> getAppliedEffects() {
        return copyIterator(appliedEffects.iterator());
    }

    /**
     * Gets the list of encountered events.
     *
     * @return Iterator of event descriptions.
     */
    public Iterator<String> getEncounteredEvents() {
        return copyIterator(encounteredEvents.iterator());
    }

    /**
     * Sets the last room swapped with (for capping recuo effect).
     *
     * @param room The room of the player swapped with.
     */
    public void setLastSwappedPosition(Room room) {
        this.lastSwappedPosition = room;
    }

    /**
     * Gets the last room swapped with.
     *
     * @return The room, or null if no swap occurred.
     */
    public Room getLastSwappedPosition() {
        return lastSwappedPosition;
    }

    /**
     * Returns a defensive-iterator backed by a copy, protecting the original collections.
     */
    private Iterator<String> copyIterator(Iterator<String> source) {
        ArrayUnorderedList<String> copy = new ArrayUnorderedList<>();
        while (source.hasNext()) {
            copy.addToRear(source.next());
        }
        return copy.iterator();
    }
}