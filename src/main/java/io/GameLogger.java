package io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Central game logger.
 * Records all important events in a log file with timestamps.
 * Used for debugging, final reporting, and evaluation.
 *
 * @author Grupo 27
 * @version 2025/2026
 */
public class GameLogger {

    private static final String LOG_FILE = "game_log.txt";
    private static PrintWriter writer;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, false)); // false = overwrite
            log("=== LABIRINTO DA GLÓRIA - INÍCIO DA SESSÃO ===");
            log("Data/Hora: " + LocalDateTime.now().format(dtf));
            // CORREÇÃO: Alterado de Grupo 47 para Grupo 27
            log("Grupo 27 - Estruturas de Dados 2025/2026");
            log("---------------------------------------------------");
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO: Não foi possível criar o ficheiro de log!");
        }
    }

    /**
     * Logs a message to the log file with a timestamp.
     *
     * @param message The message to be logged.
     */
    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(dtf);
        String line = "[" + timestamp + "] " + message;
        writer.println(line);
        writer.flush();
        System.out.println(line);
    }

    /**
     * Logs a victory event with details.
     *
     * @param winner The name of the winner.
     * @param moves  The number of moves taken.
     */
    public static void logVictory(String winner, int moves) {
        log("VITÓRIA! Jogador '" + winner + "' encontrou o tesouro em " + moves + " movimentos!");
        log("=== FIM DA PARTIDA - VENCEDOR: " + winner + " ===");
        log("==================================================");
    }

    /**
     * Closes the logger resource.
     * This method should be called at the end of the program to ensure all data is written.
     */
    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}