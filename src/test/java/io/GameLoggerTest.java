package io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class GameLoggerTest {

    @Test
    void testLogAndFileCreation() {
        // Executa métodos de log
        assertDoesNotThrow(() -> GameLogger.log("Teste Unitário - Mensagem Simples"));
        assertDoesNotThrow(() -> GameLogger.logVictory("TesterBot", 5));

        // Verifica se o ficheiro foi criado
        File logFile = new File("game_log.txt");
        assertTrue(logFile.exists(), "O ficheiro game_log.txt devia ter sido criado.");
        assertTrue(logFile.length() > 0, "O ficheiro de log não devia estar vazio.");
    }

    @AfterAll
    static void tearDown() {
        // Fecha o logger no final para garantir que o buffer é despejado
        GameLogger.close();
    }
}