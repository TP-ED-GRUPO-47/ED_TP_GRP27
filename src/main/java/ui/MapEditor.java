package ui;

import java.io.FileWriter;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Center;
import model.Corridor;
import model.Effect;
import model.Entrance;
import model.Maze;
import model.RandomEvent;
import model.RiddleRoom;
import model.Room;
import model.RoomStandard;

/**
 * Text-based Map Editor.
 * <p>
 * Allows the user to create new mazes manually by adding rooms and corridors,
 * and saving the result to a JSON file.
 * This class fulfills the requirement: "create and save new maps, reusable in
 * future matches."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class MapEditor {
    private static Scanner scanner = new Scanner(System.in);
    private static Maze maze = new Maze();

    /** Prevent instantiation; all members are static. */
    private MapEditor() { }

    /**
     * Starts the Map Editor interface.
     * <p>
     * Displays a menu loop allowing the user to add rooms, add corridors,
     * list current components, or save the map to a file.
     * </p>
     */
    public static void start() {
        scanner = new Scanner(System.in);
        maze = new Maze();
        while (true) {
            System.out.println("\n1. Adicionar Sala");
            System.out.println("2. Adicionar Corredor");
            System.out.println("3. Listar Salas");
            System.out.println("4. Guardar Mapa");
            System.out.println("5. Adicionar Corredor com Evento");
            System.out.println("0. Sair");
            System.out.print("\nEscolha uma opção: ");

            int op = readInt();
            switch (op) {
                case 1 -> addRoom();
                case 2 -> addCorridor();
                case 3 -> System.out.println(maze);
                case 4 -> saveMap();
                case 5 -> addCorridorWithEvent();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opção inválida.");
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
     * Adds a corridor with an optional event attached.
     */
    private static void addCorridorWithEvent() {
        System.out.print("De (ID): ");
        String from = scanner.nextLine();
        System.out.print("Para (ID): ");
        String to = scanner.nextLine();
        System.out.print("Custo: ");
        double cost = readDouble();
        RandomEvent event = promptEvent();
        maze.addCorridor(from, to, cost, event);
    }

    /**
     * Prompts the user to optionally attach a random event to a corridor.
     *
     * @return a RandomEvent or null if none selected
     */
    private static RandomEvent promptEvent() {
        System.out.print("Adicionar evento ao corredor (S/N)? ");
        String resp = scanner.nextLine().trim().toLowerCase();
        if (!resp.startsWith("s")) {
            return null;
        }

        System.out.print("Descrição do evento: ");
        String desc = scanner.nextLine().trim();
        if (desc.isEmpty()) {
            desc = "Evento misterioso";
        }

        System.out.println("Efeito (opcional). Escolhe uma das opções ou deixa vazio:");
        for (Effect eff : Effect.values()) {
            System.out.println(" - " + eff.name());
        }
        System.out.print("Efeito: ");
        String effRaw = scanner.nextLine().trim().toUpperCase();

        Effect eff = null;
        if (!effRaw.isEmpty()) {
            try {
                eff = Effect.valueOf(effRaw);
            } catch (IllegalArgumentException ex) {
                System.out.println("Efeito desconhecido. O evento será apenas descritivo.");
            }
        }

        return new RandomEvent(desc, eff);
    }

    /**
     * Saves the current maze configuration to a JSON file.
     * <p>
     * Serializes all rooms and corridors into proper JSON format.
     * The file is saved in the `src/main/resources/` directory.
     * </p>
     */
    private static void saveMap() {
        System.out.print("Nome do ficheiro (sem .json): ");
        String name = scanner.nextLine();

        JSONObject mazeJson = new JSONObject();
        mazeJson.put("nome", (Object) name);

        JSONArray salas = new JSONArray();
        Iterator<Room> roomsIt = maze.getAllRooms();
        while (roomsIt.hasNext()) {
            Room room = roomsIt.next();
            JSONObject roomJson = new JSONObject();
            roomJson.put("id", (Object) room.getId());
            roomJson.put("tipo", (Object) getTipoFromRoom(room));
            roomJson.put("descricao", (Object) room.getDescription());
            salas.add((Object) roomJson);
        }
        mazeJson.put("salas", (Object) salas);

        JSONArray ligacoes = new JSONArray();
        Iterator<Corridor> corridorsIt = maze.getAllCorridors();
        while (corridorsIt.hasNext()) {
            Corridor corridor = corridorsIt.next();
            JSONObject ligacao = new JSONObject();
            ligacao.put("origem", (Object) corridor.getSource().getId());
            ligacao.put("destino", (Object) corridor.getTarget().getId());
            ligacao.put("custo", (Object) corridor.getWeight());

            if (corridor.getEvent() != null) {
                JSONObject evt = new JSONObject();
                evt.put("descricao", (Object) corridor.getEvent().getDescription());
                if (corridor.getEvent().getDirectEffect() != null) {
                    evt.put("efeito", (Object) corridor.getEvent().getDirectEffect().name());
                }
                ligacao.put("evento", (Object) evt);
            }
            ligacoes.add((Object) ligacao);
        }
        mazeJson.put("ligacoes", (Object) ligacoes);

        try (FileWriter fw = new FileWriter("src/main/resources/" + name + ".json")) {
            fw.write(mazeJson.toJSONString());
            fw.flush();
            System.out.println("✓ Mapa guardado como " + name + ".json");
        } catch (Exception e) {
            System.out.println("✗ Erro ao guardar: " + e.getMessage());
        }
    }

    /**
     * Determines the room type string from a Room instance.
     */
    private static String getTipoFromRoom(Room room) {
        if (room instanceof Entrance) {
            return "ENTRADA";
        } else if (room instanceof Center) {
            return "TESOURO";
        } else if (room instanceof RiddleRoom) {
            return "ENIGMA";
        } else {
            return "NORMAL";
        }
    }

    /**
     * Helper method to safely read an integer from the scanner.
     * Consumes invalid input until a valid integer is provided.
     *
     * @return The integer read.
     */
    private static int readInt() {
        while (!scanner.hasNextInt()){
             scanner.next();
        }
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
        while (!scanner.hasNextDouble()){
            scanner.next();
        }
        double v = scanner.nextDouble();
        scanner.nextLine();
        return v;
    }
}