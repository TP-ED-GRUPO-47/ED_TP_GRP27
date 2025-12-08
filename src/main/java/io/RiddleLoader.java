package io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.Riddle;
import structures.linear.ArrayUnorderedList;

public class RiddleLoader {

    public static ArrayUnorderedList<Riddle> loadRiddles(String fileName) {
        ArrayUnorderedList<Riddle> riddles = new ArrayUnorderedList<>();
        JSONParser parser = new JSONParser();

        try (InputStream is = RiddleLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) return riddles;

            Object obj = parser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray riddlesArray = (JSONArray) jsonObject.get("enigmas");

            for (Object r : riddlesArray) {
                JSONObject riddleJson = (JSONObject) r;
                String question = (String) riddleJson.get("pergunta");
                int correct = Integer.parseInt(riddleJson.get("correta").toString());

                JSONArray optionsJson = (JSONArray) riddleJson.get("opcoes");

                ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
                for (Object opt : optionsJson) {
                    options.addToRear((String) opt);
                }

                riddles.addToRear(new Riddle(question, options, correct));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar enigmas: " + e.getMessage());
        }
        return riddles;
    }
}