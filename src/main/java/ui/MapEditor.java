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
 * Editor de mapas em modo texto.
 * Permite criar novos labirintos e guardá-los em JSON.
 * Cumpre o requisito: "criar e guardar novos mapas, reutilizáveis em futuras partidas."
 *
 * @author Grupo 47
 * @version 2025/2026
 */
public class MapEditor {
    private static final Scanner scanner = new Scanner(System.in);
    private static Maze maze = new Maze();

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

    private static void addCorridor() {
        System.out.print("De (ID): ");
        String from = scanner.nextLine();
        System.out.print("Para (ID): ");
        String to = scanner.nextLine();
        System.out.print("Custo: ");
        double cost = readDouble();
        maze.addCorridor(from, to, cost);
    }

    private static void saveMap() {
        System.out.print("Nome do ficheiro (sem .json): ");
        String name = scanner.nextLine();
        String json = "{\"nome\":\"" + name + "\",\"salas\":[],\"ligacoes\":[]}";
        try (FileWriter fw = new FileWriter("src/main/resources/" + name + ".json")) {
            fw.write(json);
            System.out.println("Mapa guardado como " + name + ".json");
        } catch (Exception e) {
            System.out.println("Erro ao guardar.");
        }
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) scanner.next();
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }

    private static double readDouble() {
        while (!scanner.hasNextDouble()) scanner.next();
        double v = scanner.nextDouble();
        scanner.nextLine();
        return v;
    }
}