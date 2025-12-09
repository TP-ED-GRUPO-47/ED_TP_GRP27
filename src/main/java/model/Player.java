package model;

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

    /**
     * Movement history used for the final report.
     * Stores the IDs of the rooms visited.
     */
    private LinkedStack<String> movementHistory;

    /**
     * Constructs a new Player.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.power = 100; // Default starting power
        this.movementHistory = new LinkedStack<>();
        this.skipNextTurn = false;
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
     * Auxiliary method to print the movement history to the console (for debugging).
     */
    public void printHistory() {
        System.out.println("Histórico de " + name + ": " + movementHistory.toString());
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
        System.out.println("⚡ " + name + " Power: " + (power - amount) + " -> " + power);

        if (this.power < 0) this.power = 0;
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
}