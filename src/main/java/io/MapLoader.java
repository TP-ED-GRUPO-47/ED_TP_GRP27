package io;

import model.*;
import model.RandomEvent;
import model.Item;
import model.Effect;

import structures.linear.ArrayUnorderedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Utility class responsible for loading Maze configurations from JSON files.
 * <p>
 * This class parses the JSON structure to create the rooms (Vertices) and corridors (Edges)
 * of the maze. It handles the creation of specific room types (Entrance, Treasure, Riddle, Lever)
 * and assigns Random Events to corridors.
 * </p>
 * <p>
 * [cite_start]It fulfills the requirement: "The mazes are defined from external files, in JSON format[cite: 25]."
 * </p>
 *
 * @author Grupo 27
 * @version 2025/2026
 */
public class MapLoader {

    /**
     * Temporary list to store riddles loaded from the external file.
     * These are assigned to RiddleRooms as the map is being built.
     */
    private static ArrayUnorderedList<Riddle> availableRiddles;

    /**
     * Loads a Maze instance from a specified JSON file located in the resources folder.
     * <p>
     * The process involves:
     * 1. Loading available riddles from "enigmas.json".
     * 2. Parsing the JSON file to extract map metadata.
     * 3. Creating Room objects based on their type using a factory method.
     * 4. Creating connections (Corridors) between rooms, including weights and optional Random Events.
     * </p>
     *
     * @param fileName The name of the JSON file to load (e.g., "map_v1.json").
     * @return A fully constructed {@link Maze} object, or an empty Maze if loading fails.
     */
    public static Maze loadMaze(String fileName) {
        Maze maze = new Maze();
        JSONParser parser = new JSONParser();

        availableRiddles = RiddleLoader.loadRiddles("enigmas.json");

        try (InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new IOException("File not found in resources: " + fileName);

            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            String mapName = (String) jsonObject.get("nome");
            System.out.println("Loading map: " + mapName);

            JSONArray roomsList = (JSONArray) jsonObject.get("salas");
            for (Object roomObj : roomsList) {
                JSONObject roomJson = (JSONObject) roomObj;
                String id = (String) roomJson.get("id");
                String type = (String) roomJson.get("tipo");
                String description = (String) roomJson.get("descricao");

                Room newRoom = createRoomFromType(id, type, description);
                if (newRoom != null) maze.addRoom(newRoom);
            }

            if (jsonObject.containsKey("ligacoes")) {
                JSONArray edgesList = (JSONArray) jsonObject.get("ligacoes");
                for (Object edgeObj : edgesList) {
                    JSONObject edgeJson = (JSONObject) edgeObj;

                    String from = (String) edgeJson.get("origem");
                    String to = (String) edgeJson.get("destino");
                    double weight = 1.0;
                    if (edgeJson.get("custo") != null) {
                        weight = Double.parseDouble(edgeJson.get("custo").toString());
                    }

                    RandomEvent event = null;
                    if (edgeJson.containsKey("evento")) {
                        JSONObject eventJson = (JSONObject) edgeJson.get("evento");
                        String evtDesc = (String) eventJson.get("descricao");
                        String evtEffectStr = (String) eventJson.get("efeito");
                        String evtItemName = (String) eventJson.get("item");

                        Effect effect = null;
                        if (evtEffectStr != null) {
                            try {
                                effect = Effect.valueOf(evtEffectStr.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                System.err.println("Unknown effect in JSON: " + evtEffectStr);
                            }
                        }

                        Item item = null;
                        if (evtItemName != null) {
                            item = new Item(evtItemName, effect);
                        }

                        event = new RandomEvent(evtDesc, item, effect);
                    }

                    maze.addCorridor(from, to, weight, event);
                }
            }

        } catch (IOException | ParseException e) {
            System.err.println("Error loading map: " + e.getMessage());
        }
        return maze;
    }

    /**
     * Factory method to create a specific Room subclass based on the provided type string.
     * <p>
     * Handles types: "ENTRADA" (Entrance), "TESOURO"/"CENTER" (Center), "ALAVANCA" (LeverRoom),
     * and "ENIGMA" (RiddleRoom). Defaults to RoomStandard.
     * </p>
     *
     * @param id          The unique identifier of the room.
     * @param type        The type string from the JSON.
     * @param description The text description of the room.
     * @return A specific {@link Room} instance.
     */
    private static Room createRoomFromType(String id, String type, String description) {
        if (type == null) return new RoomStandard(id, description);

        switch (type.toUpperCase()) {
            case "ENTRADA": return new Entrance(id, description);
            case "TESOURO":
            case "CENTER": return new Center(id, description);
            case "ALAVANCA": return new LeverRoom(id, description);
            case "ENIGMA":
                Riddle r = null;
                if (availableRiddles != null && !availableRiddles.isEmpty()) {
                    r = availableRiddles.removeFirst();
                }
                return new RiddleRoom(id, description, r);
            default: return new RoomStandard(id, description);
        }
    }
}