package io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.Riddle;
import structures.linear.ArrayUnorderedList;

/**
 * Utility class responsible for loading riddle data from JSON files.
 * <p>
 * This class parses an external JSON file (e.g., "enigmas.json") and converts
 * the data into a list of {@link Riddle} objects using the group's custom
 * {@link ArrayUnorderedList} structure.
 * </p>
 * <p>
 * [cite_start]It fulfills the requirement[cite: 37]: "An external file (JSON) must contain several questions...
 * used for random selection."
 * </p>
 *
 * @author Grupo 27
 * @version 2025/2026
 */
public class RiddleLoader {

    /**
     * Loads riddles from a specified JSON file located in the classpath resources.
     * <p>
     * The method expects a JSON structure containing an array named "enigmas",
     * where each object has:
     * <ul>
     * <li>"pergunta": The question string.</li>
     * <li>"opcoes": An array of possible answer strings.</li>
     * <li>"correta": The integer index of the correct answer.</li>
     * </ul>
     * </p>
     *
     * @param fileName The name of the JSON file to load (e.g., "enigmas.json").
     * @return An {@link ArrayUnorderedList} containing the loaded riddles. Returns an empty list if the file is not found or an error occurs.
     */
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