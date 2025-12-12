package model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;

/**
 * Unit tests for the {@link GameEngine} class.
 * <p>
 * Expanded to cover bots, manual entry choices, turn skipping, and edge cases.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class GameEngineTest {

    private final InputStream originalSystemIn = System.in;
    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        deleteIfExists("report_Winner.json");
        deleteIfExists("report_Bot_1.json");
    }

    private void deleteIfExists(String fileName) {
        File report = new File(fileName);
        if (report.exists()) {
            report.delete();
        }
    }

    /**
     * Helper method to access private fields using Reflection.
     * Useful to inspect the internal state (queue size, flags) without public
     * getters.
     */
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = GameEngine.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(gameEngine);
    }

    private Object getPrivateFieldSafe(String fieldName) {
        try {
            return getPrivateField(fieldName);
        } catch (Exception e) {
            return null;
        }
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = GameEngine.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(gameEngine, value);
    }

    /**
     * Tests successful game initialization with a test map.
     */
    @Test
    void testInitGameSuccess() {
        String input = "1\nN\nHero\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();
        gameEngine.initGame("test_map.json");

        assertDoesNotThrow(() -> {
            LinkedQueue<?> queue = (LinkedQueue<?>) getPrivateField("turnQueue");
            assertFalse(queue.isEmpty(), "Fila não deve estar vazia.");
            Boolean running = (Boolean) getPrivateField("isRunning");
            assertTrue(running, "Jogo deve estar ativo após inicializar com jogadores.");
        });
    }

    /**
     * Tests that initializing with an invalid map file is handled gracefully.
     */
    @Test
    void testInitGameInvalidMap() {
        gameEngine.initGame("mapa_que_nao_existe.json");
        LinkedQueue<?> queue = (LinkedQueue<?>) getPrivateFieldSafe("turnQueue");
        if (queue != null) {
            assertTrue(queue.isEmpty(), "Fila deve ficar vazia em mapa inválido.");
        }
        Boolean running = (Boolean) getPrivateFieldSafe("isRunning");
        if (running != null) {
            assertFalse(running, "Jogo não deve arrancar com mapa inválido.");
        }
    }

    /**
     * Tests game loop execution with player movements.
     */
    @Test
    void testInitGameWithManualEntranceChoice() {
        String input = "1\nS\nManualPlayer\n1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();
        assertDoesNotThrow(() -> gameEngine.initGame("test_map.json"));

        LinkedQueue<?> queue = (LinkedQueue<?>) getPrivateFieldSafe("turnQueue");
        if (queue != null) {
            assertFalse(queue.isEmpty());
        }
    }

    /**
     * Ensures addPlayerWithChoice coloca o jogador na entrada escolhida.
     */
    @Test
    void testAddPlayerWithChoiceSelectsRequestedEntrance() throws Exception {
        ArrayUnorderedList<Room> entrances = new ArrayUnorderedList<>();
        Room e1 = new Entrance("E1", "Entrada 1");
        Room e2 = new Entrance("E2", "Entrada 2");
        entrances.addToRear(e1);
        entrances.addToRear(e2);

        setPrivateField("availableEntrances", entrances);

        Scanner customScanner = new Scanner(new ByteArrayInputStream("2\n".getBytes()));
        setPrivateField("scanner", customScanner);

        Method addPlayerWithChoice = GameEngine.class.getDeclaredMethod("addPlayerWithChoice", Player.class);
        addPlayerWithChoice.setAccessible(true);

        Player player = new Player("Chooser");
        addPlayerWithChoice.invoke(gameEngine, player);

        assertEquals(e2, player.getCurrentRoom(), "Jogador deve ficar na entrada escolhida (2).");

        LinkedQueue<?> queue = (LinkedQueue<?>) getPrivateField("turnQueue");
        assertEquals(1, queue.size(), "Fila deve conter o jogador adicionado.");
    }

    /**
     * Tests manual entrance choice with invalid numeric input; should fallback
     * safely.
     */
    @Test
    void testInitGameWithManualEntranceInvalidInput() {
        String input = "1\nS\nBadInputPlayer\ntexto\n1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();
        assertDoesNotThrow(() -> gameEngine.initGame("test_map.json"));
    }

    /**
     * Tests initialization with 0 players (Human and Bots).
     * Should prevent game start.
     */
    @Test
    void testNoPlayers() throws Exception {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();
        gameEngine.initGame("test_map.json");

        LinkedQueue<?> queue = (LinkedQueue<?>) getPrivateField("turnQueue");
        assertTrue(queue.isEmpty(), "A fila deve estar vazia se não houver jogadores.");

        Boolean isRunning = (Boolean) getPrivateField("isRunning");
        assertFalse(isRunning, "Jogo não deve arrancar sem jogadores.");
    }

    /**
     * Tests initialization with only Bots.
     */
    @Test
    void testBotsOnly() {
        String input = "0\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();
        gameEngine.initGame("test_map.json");

        assertDoesNotThrow(() -> {
            LinkedQueue<?> queue = (LinkedQueue<?>) getPrivateField("turnQueue");
            assertEquals(2, queue.size(), "Devem existir 2 bots na fila.");
            Boolean running = (Boolean) getPrivateField("isRunning");
            assertTrue(running, "Jogo deve arrancar com bots.");
        });
    }

    /**
     * Minimal smoke test to ensure start() returns gracefully when queue is vazia.
     */
    @Test
    void testStartWithEmptyQueue() {
        gameEngine.start();
        assertTrue(true, "start() deve terminar mesmo com fila vazia.");
    }
}