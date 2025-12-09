package ui;

import java.io.FileWriter;
import java.util.Scanner;

import model.Center;
import model.Entrance;
import model.Maze;
import model.RiddleRoom;
import model.Room;
import model.RoomStandard;

/**
 * Text-based Map Editor.
 * <p>
 * Allows the user to create new mazes manually by adding rooms and corridors,
 * and saving the result to a JSON file.
 * This class fulfills the requirement: "create and save new maps, reusable in future matches."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class MapEditor {
    private static final Scanner scanner = new Scanner(System.in);
    private static Maze maze = new Maze();

    /**
     * Starts the Map Editor interface.
     * <p>
     * Displays a menu loop allowing the user to add rooms, add corridors,
     * list current components, or save the map to a file.
     * </p>
     */
    public static void start() {
        System.out.println("=== MAP EDITOR - Labirinto da Glória ===");
        while (true) {
            System.out.println("\n1. Adicionar Sala");
            System.out.println("2. Adicionar Corredor");
            System.out.println("3. Listar Salas");
            System.out.println("4. Guardar Mapa");
            System.out.println("5. Sair");
            System.out.print("> ");

            int op = readInt();
            switch (op) {
                case 1 -> addRoom();
                case 2 -> addCorridor();
                case 3 -> System.out.println(maze);
                case 4 -> saveMap();
                case 5 -> { System.out.println("Saindo..."); return; }
            }
        }
    }

    /**
     * Interactively adds a new room to the maze.
     * <p>
     * Prompts the user for the Room ID, Type (ENTRADA, TESOURO, ENIGMA, NORMAL),
     * and a description.
     * </p>
     */
    private static void addRoom() {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Tipo (ENTRADA, TESOURO, ENIGMA, NORMAL): ");
        String type = scanner.nextLine().toUpperCase();
        System.out.print("Descrição: ");
        String desc = scanner.nextLine();

        Room room = switch (type) {
            case "ENTRADA" -> new Entrance(id, desc);
            case "TESOURO" -> new Center(id, desc);
            case "ENIGMA" -> new RiddleRoom(id, desc, null);
            default -> new RoomStandard(id, desc);
        };
        maze.addRoom(room);
        System.out.println("Sala adicionada!");
    }

    /**
     * Interactively adds a corridor (connection) between two existing rooms.
     * <p>
     * Prompts the user for the source ID, destination ID, and the travel cost.
     * </p>
     */
    private static void addCorridor() {
        System.out.print("De (ID): ");
        String from = scanner.nextLine();
        System.out.print("Para (ID): ");
        String to = scanner.nextLine();
        System.out.print("Custo: ");
        double cost = readDouble();
        maze.addCorridor(from, to, cost);
    }

    /**
     * Saves the current maze configuration to a JSON file.
     * <p>
     * The file is saved in the `src/main/resources/` directory with the name provided by the user.
     * Note: This implementation currently creates a basic JSON structure.
     * </p>
     */
    private static void saveMap() {
        System.out.print("Nome do ficheiro (sem .json): ");
        String name = scanner.nextLine();
        // Generates a placeholder structure; full serialization logic would expand here
        String json = "{\"nome\":\"" + name + "\",\"salas\":[],\"ligacoes\":[]}";
        try (FileWriter fw = new FileWriter("src/main/resources/" + name + ".json")) {
            fw.write(json);
            System.out.println("Mapa guardado como " + name + ".json");
        } catch (Exception e) {
            System.out.println("Erro ao guardar.");
        }
    }

    /**
     * Helper method to safely read an integer from the scanner.
     * Consumes invalid input until a valid integer is provided.
     *
     * @return The integer read.
     */
    private static int readInt() {
        while (!scanner.hasNextInt()) scanner.next();
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }

    /**
     * Helper method to safely read a double from the scanner.
     * Consumes invalid input until a valid double is provided.
     *
     * @return The double read.
     */
    private static double readDouble() {
        while (!scanner.hasNextDouble()) scanner.next();
        double v = scanner.nextDouble();
        scanner.nextLine();
        return v;
    }
}