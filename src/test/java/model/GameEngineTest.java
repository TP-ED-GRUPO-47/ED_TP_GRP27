package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

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

        File report = new File("report_Winner.json");
        if(report.exists()) report.delete();
    }

    // --- TESTES DE INICIALIZAÇÃO ---

    @Test
    void testInitGameSuccess() {
        String input = "1\nHero\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();

        gameEngine.initGame("test_map.json");

    }

    @Test
    void testInitGameInvalidMap() {
        gameEngine.initGame("mapa_que_nao_existe.json");
    }

    // --- TESTES DE LÓGICA DE JOGO (SIMULAÇÃO) ---

    @Test
    void testGameLoopAndMovement() {

        String input =
                // Init
                "1\nPlayerOne\n0\n" +
                        "move S1\n" +
                        "move R1\n" +
                        "exit\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        gameEngine = new GameEngine();

        gameEngine.initGame("test_map.json");
        assertDoesNotThrow(() -> gameEngine.start());
    }

    @Test
    void testWinCondition() {

        String input = "1\nWinner\n0\nmove S1\nmove R1\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();
        gameEngine.initGame("test_map.json");
        gameEngine.start();
    }

    @Test
    void testLeverInteraction() {


        String input = "1\nLeverTester\n0\nmove S1\nmove L1\nsim\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameEngine = new GameEngine();
        gameEngine.initGame("test_map.json");
        assertDoesNotThrow(() -> gameEngine.start());
    }
}