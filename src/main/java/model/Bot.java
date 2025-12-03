package model;

import java.util.Iterator;
import java.util.Random;
import structures.linear.ArrayUnorderedList;

public class Bot extends Player {

    private Random random;

    public Bot(String name) {
        super(name);
        this.random = new Random();
    }

    /**
     * O Bot decide para onde ir de forma ALEATÓRIA.
     * @param maze O labirinto atual.
     * @return O ID da sala para onde ele quer ir.
     */
    public String decideMove(Maze maze) {
        Room current = getCurrentRoom();

        if (current == null) return null;

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
            System.out.println(getName() + " (Bot) escolheu aleatoriamente ir para: " + chosenRoom.getId());
            return chosenRoom.getId();
        }

        return null;
    }
}