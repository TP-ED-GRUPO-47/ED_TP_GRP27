package ui;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

import model.GameEngine;
import structures.linear.ArrayUnorderedList;

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
     * Displays the main menu and processes user choices until the "Exit" option is
     * selected.
     * </p>
     */
    public void run() {
        boolean running = true;

        System.out.println("Labirinto da Glória!\n");

        while (running) {
            System.out.println("Menu Principal:\n");
            System.out.println("1. Novo Jogo");
            System.out.println("2. Editor de Mapas");
            System.out.println("3. Sair\n");
            System.out.print("Escolha uma opção: ");

            int option = readInt();

            switch (option) {
                case 1:
                    startGame();
                    break;
                case 2:
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
     * Prompts the user to select a map from available options and
     * transfers control to the {@link GameEngine}.
     * </p>
     */
    private void startGame() {
        GameEngine game = new GameEngine();

        System.out.println("\nSeleção de Mapa:\n");

        String[][] orderedMaps = {
            {"mapa_facil.json", "Arena de Treino do Iniciado", "Fácil"},
            {"mapa_medio.json", "O Castelo Abandonado de Aldoria", "Médio"},
            {"mapa_dificil.json", "O Grande Labirinto da Glória e do Desespero", "Difícil"},
            {"mapa_epico.json", "A Dungeon Épica dos Desafios e da Glória", "Épico"}
        };

        ArrayUnorderedList<String> availableMaps = discoverAvailableMaps();
        
        if (availableMaps.isEmpty()) {
            System.out.println("Nenhum mapa foi encontrado!");
            System.out.println();
            return;
        }

        int displayIndex = 1;
        ArrayUnorderedList<String> displayedMapFiles = new ArrayUnorderedList<>();

        for (String[] mapInfo : orderedMaps) {
            String mapFile = mapInfo[0];
            String mapName = mapInfo[1];
            String difficulty = mapInfo[2];
            
            if (mapExists(availableMaps, mapFile)) {
                System.out.println(displayIndex + ". " + mapName + " (" + difficulty + ")");
                displayedMapFiles.addToRear(mapFile);
                displayIndex++;
            }
        }

        System.out.println(displayIndex + ". Carregar Mapa Personalizado");
        System.out.println("0. Sair");
        System.out.println();
        System.out.print("Escolha uma Opção: ");

        int mapChoice = readInt();
        String mapName;

        if (mapChoice == 0) {
            System.out.println();
            return;
        } else if (mapChoice >= 1 && mapChoice < displayIndex) {
            Iterator<String> mapIt = displayedMapFiles.iterator();
            for (int i = 1; i < mapChoice; i++) {
                mapIt.next();
            }
            mapName = mapIt.next();
        } else if (mapChoice == displayIndex) {
            System.out.print("\nNome do ficheiro .json: ");
            mapName = scanner.nextLine().trim();
            if (!mapName.endsWith(".json")) {
                mapName += ".json";
            }
        } else {
            System.out.println();
            System.out.println("Opção inválida. A voltar ao menu...");
            return;
        }

        game.initGame(mapName);
        game.start();
    }

    /**
     * Verifica se um ficheiro de mapa específico existe na lista de mapas disponíveis.
     *
     * @param availableMaps Lista de mapas disponíveis.
     * @param mapFile Nome do ficheiro a procurar.
     * @return true se o mapa existe, false caso contrário.
     */
    private boolean mapExists(ArrayUnorderedList<String> availableMaps, String mapFile) {
        Iterator<String> it = availableMaps.iterator();
        while (it.hasNext()) {
            if (it.next().equals(mapFile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Discovers all .json files in the src/main/resources directory.
     * <p>
     * This allows dynamic listing of available maps, including user-created ones.
     * </p>
     *
     * @return ArrayUnorderedList of JSON filenames found in resources.
     */
    private ArrayUnorderedList<String> discoverAvailableMaps() {
        ArrayUnorderedList<String> maps = new ArrayUnorderedList<>();

        File resourcesDir = new File("src/main/resources");
        if (resourcesDir.exists() && resourcesDir.isDirectory()) {
            File[] files = resourcesDir
                    .listFiles((dir, name) -> name.endsWith(".json") && !name.equals("enigmas.json"));
            if (files != null) {
                for (File file : files) {
                    maps.addToRear(file.getName());
                }
            }
        }

        if (maps.isEmpty()) {
            InputStream testStream = getClass().getClassLoader().getResourceAsStream("mapa_medio.json");
            if (testStream != null) {
                maps.addToRear("mapa_facil.json");
                maps.addToRear("mapa_medio.json");               
                maps.addToRear("mapa_dificil.json");
                maps.addToRear("mapa_epico.json");
                try {
                    testStream.close();
                } catch (Exception ignored) {
                }
            }
        }

        return maps;
    }

    /**
     * Helper method to safely read an integer from the user.
     * <p>
     * It handles invalid input (non-integers) by consuming the token and prompting
     * again.
     * </p>
     *
     * @return A valid integer entered by the user.
     */
    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, insere um número válido!");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}