package io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger central do jogo.
 * Regista todos os eventos importantes num ficheiro de log com timestamp.
 * Usado para depuração, relatório final e avaliação.
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public class GameLogger {

    private static final String LOG_FILE = "game_log.txt";
    private static PrintWriter writer;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, false)); // false = sobrescreve
            log("=== LABIRINTO DA GLÓRIA - INÍCIO DA SESSÃO ===");
            log("Data/Hora: " + LocalDateTime.now().format(dtf));
            log("Grupo 47 - Estruturas de Dados 2025/2026");
            log("---------------------------------------------------");
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO: Não foi possível criar o ficheiro de log!");
        }
    }

    /**
     * Regista uma mensagem no ficheiro de log com timestamp.
     *
     * @param message Mensagem a registar
     */
    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(dtf);
        String line = "[" + timestamp + "] " + message;
        writer.println(line);
        writer.flush();
        System.out.println(line);
    }

    /**
     * Regista uma vitória com detalhes.
     *
     * @param winner Nome do vencedor
     * @param moves  Número de movimentos
     */
    public static void logVictory(String winner, int moves) {
        log("VITÓRIA! Jogador '" + winner + "' encontrou o tesouro em " + moves + " movimentos!");
        log("=== FIM DA PARTIDA - VENCEDOR: " + winner + " ===");
        log("==================================================");
    }

    /**
     * Fecha o logger (chamar no fim do programa).
     */
    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}