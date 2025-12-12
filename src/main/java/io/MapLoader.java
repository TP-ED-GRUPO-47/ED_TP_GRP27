package io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import exceptions.InvalidJsonException;
import model.Center;
import model.Effect;
import model.Entrance;
import model.LeverRoom;
import model.Maze;
import model.RandomEvent;
import model.Riddle;
import model.RiddleRoom;
import model.Room;
import model.RoomStandard;
import structures.linear.ArrayUnorderedList;

/**
 * Utility class responsible for loading Maze configurations from JSON files.
 * <p>
 * This class parses the JSON structure to create the rooms (Vertices) and corridors (Edges)
 * of the maze. It handles the creation of specific room types (Entrance, Treasure, Riddle, Lever)
 * and assigns Random Events to corridors.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class MapLoader {

    /**
     * Utility class; prevent instantiation.
     */
    private MapLoader() { }

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

        ArrayUnorderedList<Riddle> availableRiddles = RiddleLoader.loadRiddles("enigmas.json");
        ArrayUnorderedList<Riddle> usedRiddles = new ArrayUnorderedList<>();

        try (InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                System.err.println("\nFicheiro não encontrado: " + fileName);
                return maze;
            }

            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            if (jsonObject == null) {
                System.err.println("Ficheiro JSON vazio ou inválido: " + fileName);
                return maze;
            }

            String mapName = (String) jsonObject.get("nome");
            if (mapName == null) {
                System.err.println("Campo 'nome' não encontrado no mapa");
                mapName = "Mapa Desconhecido";
            }
            System.out.println("✓ Carregado Mapa: " + mapName + "!");

            JSONArray roomsList = (JSONArray) jsonObject.get("salas");
            if (roomsList == null || roomsList.isEmpty()) {
                System.err.println("Nenhuma sala ('salas') encontrada no mapa JSON");
                return maze;
            }

            int roomCount = 0;
            for (Object roomObj : roomsList) {
                try {
                    JSONObject roomJson = (JSONObject) roomObj;
                    String id = (String) roomJson.get("id");
                    String type = (String) roomJson.get("tipo");
                    String description = (String) roomJson.get("descricao");

                    if (id == null || id.isEmpty()) {
                        System.err.println("Sala sem ID ignorada");
                        continue;
                    }

                    Room newRoom = createRoomFromType(id, type, description, availableRiddles, usedRiddles);
                    if (newRoom != null) {
                        maze.addRoom(newRoom);
                        roomCount++;
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar sala: " + e.getMessage());
                }
            }
            System.out.println("✓ Carregados " + availableRiddles.size() + " enigmas!");
            System.out.println("✓ Carregadas " + roomCount + " salas!");

            if (jsonObject.containsKey("ligacoes")) {
                JSONArray edgesList = (JSONArray) jsonObject.get("ligacoes");
                if (edgesList != null) {
                    int corridorCount = 0;
                    for (Object edgeObj : edgesList) {
                        try {
                            JSONObject edgeJson = (JSONObject) edgeObj;

                            String from = (String) edgeJson.get("origem");
                            String to = (String) edgeJson.get("destino");

                            if (from == null || to == null || from.isEmpty() || to.isEmpty()) {
                                System.err.println("Corredor com IDs inválidos ignorado");
                                continue;
                            }

                            double weight = 1.0;
                            if (edgeJson.get("custo") != null) {
                                try {
                                    weight = Double.parseDouble(edgeJson.get("custo").toString());
                                } catch (NumberFormatException e) {
                                    System.err.println("Custo não numérico para corredor " + from + "->" + to + ", usando 1.0");
                                    weight = 1.0;
                                }
                            }

                            RandomEvent event = null;
                            if (edgeJson.containsKey("evento")) {
                                try {
                                    JSONObject eventJson = (JSONObject) edgeJson.get("evento");
                                    String evtDesc = (String) eventJson.get("descricao");
                                    String evtEffectStr = (String) eventJson.get("efeito");

                                    Effect effect = null;
                                    if (evtEffectStr != null && !evtEffectStr.isEmpty()) {
                                        try {
                                            effect = Effect.valueOf(evtEffectStr.toUpperCase());
                                        } catch (IllegalArgumentException e) {
                                            System.err.println("Efeito desconhecido no JSON: " + evtEffectStr);
                                        }
                                    }

                                    event = new RandomEvent(evtDesc != null ? evtDesc : "Evento misterioso", effect);
                                } catch (Exception e) {
                                    System.err.println("Erro ao processar evento no corredor: " + e.getMessage());
                                }
                            }

                            maze.addCorridor(from, to, weight, event);
                            corridorCount++;
                        } catch (Exception e) {
                            System.err.println("Erro ao processar corredor: " + e.getMessage());
                        }
                    }
                    System.out.println("✓ Carregados " + corridorCount + " corredores!");
                }
            } else {
                System.err.println("Nenhum corredor ('ligacoes') encontrado no mapa");
            }

        } catch (IOException e) {
            System.err.println("Erro de leitura de ficheiro: " + e.getMessage());
            throw new InvalidJsonException("Erro ao ler ficheiro JSON: " + fileName);
        } catch (ParseException e) {
            System.err.println("Erro ao analisar JSON: " + e.getMessage());
            throw new InvalidJsonException("JSON inválido em " + fileName + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao carregar mapa: " + e.getMessage());
            e.printStackTrace();
            throw new InvalidJsonException("Erro inesperado ao processar JSON: " + e.getMessage());
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
    private static Room createRoomFromType(String id, String type, String description,
                                           ArrayUnorderedList<Riddle> availableRiddles,
                                           ArrayUnorderedList<Riddle> usedRiddles) {
        if (type == null){
            return new RoomStandard(id, description);
        } 

        switch (type.toUpperCase()) {
            case "ENTRADA": return new Entrance(id, description);
            case "TESOURO":
            case "CENTER": return new Center(id, description);
            case "ALAVANCA": return new LeverRoom(id, description);
            case "ENIGMA":
                Riddle r = null;
                if (availableRiddles != null) {
                    if (availableRiddles.isEmpty() && usedRiddles != null && !usedRiddles.isEmpty()) {
                        System.out.println("Cycling riddles: all riddles have been used, restarting pool.");
                        while (!usedRiddles.isEmpty()) {
                            availableRiddles.addToRear(usedRiddles.removeFirst());
                        }
                    }
                    
                    if (!availableRiddles.isEmpty()) {
                        r = availableRiddles.removeFirst();
                        if (usedRiddles != null) {
                            usedRiddles.addToRear(r);
                        }
                    }
                }
                return new RiddleRoom(id, description, r);
            default: return new RoomStandard(id, description);
        }
    }
}