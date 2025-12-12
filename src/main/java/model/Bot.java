package model;

import java.util.Iterator;
import java.util.Random;

import structures.linear.ArrayUnorderedList;

/**
 * Represents an automated player (Bot) in the Glory Maze.
 * <p>
 * The Bot operates autonomously with a specific personality decided at creation time.
 * It can be "Smart" (prioritizing the shortest path to the treasure) or "Random" (moving blindly).
 * Even smart bots have a small probability of error to simulate human distraction.
 * </p>
 * <p>
 * It fulfills the requirement for the "Automatic Mode" and "Simple Strategies".
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Bot extends Player {

    private Random random;
    private boolean isSmart;
    private static final double ERROR_CHANCE = 0.2;

    /**
     * Constructs a new Bot with the specified name.
     * <p>
     * Initializes the random number generator and randomly assigns a strategy
     * (50% chance to be Smart, 50% chance to be Random).
     * </p>
     *
     * @param name The name of the bot.
     */
    public Bot(String name) {
        super(name);
        this.random = new Random();

        this.isSmart = random.nextBoolean();

        String strategyName = isSmart ? "INTELIGENTE" : "ALEATÓRIA";
        System.out.println("\nBot criado: " + name + " (" + strategyName + ")");
    }

    /**
     * Decides the next move for the bot based on its assigned strategy.
     * <p>
     * If the bot is <b>Smart</b>:
     * 1. It calculates the shortest path to the {@link Center} (Treasure).
     * 2. Has a 20% chance to get "distracted" and make a random move instead.
     * <br>
     * If the bot is <b>Random</b> (or distracted):
     * 1. It picks a random neighbor from the current room.
     * </p>
     *
     * @param maze The current maze instance used to check neighbors and calculate paths.
     * @return The ID of the target room to move to, or null if the bot is stuck.
     */
    public String decideMove(Maze maze) {
        Room current = getCurrentRoom();
        if (current == null){
            return null;
        } 

        if (isSmart) {
            if (random.nextDouble() < ERROR_CHANCE) {
                System.out.println(getName() + " distraiu-se e vai mover-se aleatoriamente!");
            } else {
                Room treasure = maze.getTreasureRoom();
                if (treasure != null) {
                    Iterator<Room> pathIt = maze.getShortestPath(current, treasure);

                    if (pathIt.hasNext()) {
                        pathIt.next();

                        if (pathIt.hasNext()) {
                            Room nextRoom = pathIt.next();
                            System.out.println(getName() + " calculou a rota ótima para: " + nextRoom.getId());
                            return nextRoom.getId();
                        }
                    }
                }
            }
        }

        ArrayUnorderedList<Room> neighborsList = new ArrayUnorderedList<>();
        Iterator<Room> it = maze.getNeighbors(current);

        int count = 0;
        while (it.hasNext()) {
            neighborsList.addToRear(it.next());
            count++;
        }

        if (count == 0) {
            System.out.println(getName() + " (Bot) está num beco sem saída!");
            return null;
        }

        int randomIndex = random.nextInt(count);
        Iterator<Room> choiceIt = neighborsList.iterator();
        Room chosenRoom = null;

        for (int i = 0; i <= randomIndex; i++) {
            chosenRoom = choiceIt.next();
        }

        if (chosenRoom != null) {
            String strategyLabel = isSmart ? "(Fallback)" : "(Random)";
            System.out.println(getName() + " " + strategyLabel + " escolheu ir para: " + chosenRoom.getId());
            return chosenRoom.getId();
        }

        return null;
    }
}