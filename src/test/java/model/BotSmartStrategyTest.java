package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.linear.ArrayUnorderedList;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes avançados para a estratégia do Bot.
 * Usa Reflection e Mocking para forçar comportamentos aleatórios.
 */
class BotSmartStrategyTest {

    private Bot bot;
    private Maze maze;
    private Room start;
    private Room pathRoom;
    private Room deadEnd;
    private Room treasure;

    /**
     * Classe Mock para controlar o comportamento do Random.
     * Permite definir o que sai no nextDouble() e no nextInt().
     */
    private static class ControlledRandom extends Random {
        private final double doubleVal;
        private final int intVal;

        public ControlledRandom(double doubleVal, int intVal) {
            this.doubleVal = doubleVal;
            this.intVal = intVal;
        }

        @Override
        public double nextDouble() {
            return doubleVal;
        }

        @Override
        public int nextInt(int bound) {
            return intVal % bound;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        bot = new Bot("SmartTestBot");
        maze = new Maze();

        start = new RoomStandard("Start", "Inicio");
        pathRoom = new RoomStandard("Path", "Caminho Certo");
        deadEnd = new RoomStandard("Dead", "Caminho Errado");
        treasure = new Center("Treasure", "Tesouro");

        maze.addRoom(start);
        maze.addRoom(pathRoom);
        maze.addRoom(deadEnd);
        maze.addRoom(treasure);

        // Definir conexões
        maze.addCorridor("Start", "Path", 1.0);
        maze.addCorridor("Path", "Treasure", 1.0);
        maze.addCorridor("Start", "Dead", 1.0); // Opção errada

        bot.setCurrentRoom(start);

        setPrivateField(bot, "isSmart", true);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testSmartLogicSuccess() throws Exception {

        Random notDistractedRandom = new ControlledRandom(0.9, 0);
        setPrivateField(bot, "random", notDistractedRandom);

        String chosenMove = bot.decideMove(maze);

        assertEquals("Path", chosenMove, "Bot inteligente focado deve escolher o caminho para o tesouro");
    }

    @Test
    void testSmartLogicDistracted() throws Exception {
        int deadEndIndex = -1;
        Iterator<Room> it = maze.getNeighbors(start);
        int idx = 0;
        while(it.hasNext()) {
            Room r = it.next();
            if(r.getId().equals("Dead")) {
                deadEndIndex = idx;
            }
            idx++;
        }

        assertTrue(deadEndIndex != -1);

        Random distractedRandom = new ControlledRandom(0.1, deadEndIndex);
        setPrivateField(bot, "random", distractedRandom);

        String chosenMove = bot.decideMove(maze);

        assertEquals("Dead", chosenMove, "Bot distraído deve ignorar o caminho ótimo e escolher aleatoriamente (neste caso forçado para Dead)");
    }

    @Test
    void testSmartLogicNoPath() throws Exception {

        Maze emptyMaze = new Maze();
        Room isolated = new RoomStandard("Iso", "Isolado");
        emptyMaze.addRoom(isolated);

        bot.setCurrentRoom(isolated);
        setPrivateField(bot, "isSmart", true);
        setPrivateField(bot, "random", new ControlledRandom(0.9, 0));

        String result = bot.decideMove(emptyMaze);
        assertNull(result);
    }
}