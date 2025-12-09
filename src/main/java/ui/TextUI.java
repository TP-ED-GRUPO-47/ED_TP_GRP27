package ui;

import model.GameEngine;
import java.util.Scanner;

/**
 * Text-based User Interface (Console) for the application.
 * <p>
 * This class handles the main menu loop, allowing the user to navigate
 * between playing the game, editing maps, or exiting the application.
 * It serves as the primary entry point for user interaction.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class TextUI {

    private final Scanner scanner;

    /**
     * Constructs a new TextUI instance.
     * Initializes the scanner for user input.
     */
    public TextUI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main application loop.
     * <p>
     * Displays the main menu and processes user choices until the "Exit" option is selected.
     * </p>
     */
    public void run() {
        boolean running = true;

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        LABIRINTO DA GLÓRIA         ║");
        System.out.println("╚════════════════════════════════════╝");

        while (running) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Novo Jogo");
            System.out.println("2. Editor de Mapas");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int option = readInt();

            switch (option) {
                case 1:
                    startGame();
                    break;
                case 2:
                    // Calls the static start method of MapEditor
                    MapEditor.start();
                    break;
                case 3:
                    System.out.println("Obrigado por jogar! Até à próxima.");
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tenta novamente.");
            }
        }
        scanner.close();
    }

    /**
     * Initializes and starts a new game session.
     * <p>
     * It prompts the user for the map file name (defaulting to "mapa_v3.json")
     * and transfers control to the {@link GameEngine}.
     * </p>
     */
    private void startGame() {
        GameEngine game = new GameEngine();

        System.out.print("Nome do mapa a carregar (Enter para 'mapa_v1.json'): ");
        String mapName = scanner.nextLine().trim();

        if (mapName.isEmpty()) {
            mapName = "mapa_v3.json";
        }

        if (!mapName.endsWith(".json")) {
            mapName += ".json";
        }

        game.initGame(mapName);
        game.start();
    }

    /**
     * Helper method to safely read an integer from the user.
     * <p>
     * It handles invalid input (non-integers) by consuming the token and prompting again.
     * </p>
     *
     * @return A valid integer entered by the user.
     */
    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println(" Por favor, insere um número válido.");
            System.out.print("> ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}