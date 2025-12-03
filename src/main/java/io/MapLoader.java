package io;

import model.*;
import model.RoomStandard;
import model.Entrance;
import model.Center;
import model.RiddleRoom;
import model.Riddle;

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

public class MapLoader {

    private static ArrayUnorderedList<Riddle> availableRiddles;

    public static Maze loadMaze(String fileName) {
        Maze maze = new Maze();
        JSONParser parser = new JSONParser();

        availableRiddles = RiddleLoader.loadRiddles("enigmas.json");

        try (InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(fileName)) {

            if (is == null) {
                throw new IOException("Ficheiro não encontrado nos resources: " + fileName);
            }

            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            String mapName = (String) jsonObject.get("nome");
            System.out.println("A carregar mapa: " + mapName);

            JSONArray roomsList = (JSONArray) jsonObject.get("salas");

            for (Object roomObj : roomsList) {
                JSONObject roomJson = (JSONObject) roomObj;

                String id = (String) roomJson.get("id");
                String type = (String) roomJson.get("tipo");
                String description = (String) roomJson.get("descricao");

                Room newRoom = createRoomFromType(id, type, description);

                if (newRoom != null) {
                    maze.addRoom(newRoom);
                }
            }

            if (jsonObject.containsKey("ligacoes")) {
                JSONArray edgesList = (JSONArray) jsonObject.get("ligacoes");

                for (Object edgeObj : edgesList) {
                    JSONObject edgeJson = (JSONObject) edgeObj;

                    String from = (String) edgeJson.get("origem");
                    String to = (String) edgeJson.get("destino");

                    double weight = 1.0;
                    Object custoObj = edgeJson.get("custo");

                    if (custoObj != null) {
                        weight = Double.parseDouble(custoObj.toString());
                    }

                    maze.addCorridor(from, to, weight);
                }
            }

        } catch (IOException | ParseException e) {
            System.err.println("Erro ao carregar o mapa: " + e.getMessage());
        }

        return maze;
    }

    private static Room createRoomFromType(String id, String type, String description) {
        if (type == null) return new RoomStandard(id, description);

        switch (type.toUpperCase()) {
            case "ENTRADA":
                return new Entrance(id, description);
            case "TESOURO":
            case "CENTER":
                return new Center(id, description);
            case "ENIGMA":
                Riddle r = null;
                if (availableRiddles != null && !availableRiddles.isEmpty()) {
                    r = availableRiddles.removeFirst();
                } else {
                    System.out.println("Aviso: Não há mais enigmas disponíveis para a sala " + id);
                }
                return new RiddleRoom(id, description, r);
            default:
                return new RoomStandard(id, description);
        }
    }
}