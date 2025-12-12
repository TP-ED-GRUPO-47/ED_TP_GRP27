package io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import exceptions.InvalidJsonException;
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
 * @author Group 27
 * @version 2025/2026
 */
public class RiddleLoader {

    /** Utility class; no instances required. */
    private RiddleLoader() { }

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
     *
     * @param fileName The name of the JSON file to load (e.g., "enigmas.json").
     * @return An {@link ArrayUnorderedList} containing the loaded riddles. Returns an empty list if the file is not found or an error occurs.
     */
    public static ArrayUnorderedList<Riddle> loadRiddles(String fileName) {
        ArrayUnorderedList<Riddle> riddles = new ArrayUnorderedList<>();
        JSONParser parser = new JSONParser();

        try (InputStream is = RiddleLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                System.err.println("Ficheiro de enigmas não encontrado: " + fileName);
                return riddles;
            }

            Object obj = parser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            if (obj == null) {
                System.err.println("Ficheiro de enigmas vazio ou inválido");
                return riddles;
            }

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray riddlesArray = (JSONArray) jsonObject.get("enigmas");

            if (riddlesArray == null || riddlesArray.isEmpty()) {
                System.err.println("Array 'enigmas' não encontrado ou vazio no ficheiro");
                return riddles;
            }

            int riddleCount = 0;
            for (Object r : riddlesArray) {
                try {
                    JSONObject riddleJson = (JSONObject) r;

                    String question = (String) riddleJson.get("pergunta");
                    if (question == null || question.isEmpty()) {
                        System.err.println("Enigma sem pergunta ignorado");
                        continue;
                    }

                    Object correctObj = riddleJson.get("correta");
                    if (correctObj == null) {
                        System.err.println("Enigma sem resposta correta ignorado");
                        continue;
                    }

                    int correct;
                    try {
                        correct = Integer.parseInt(correctObj.toString());
                    } catch (NumberFormatException e) {
                        System.err.println("Resposta correta não é um número: " + correctObj);
                        continue;
                    }

                    JSONArray optionsJson = (JSONArray) riddleJson.get("opcoes");
                    if (optionsJson == null || optionsJson.isEmpty()) {
                        System.err.println("Enigma sem opções ignorado");
                        continue;
                    }

                    if (correct < 0 || correct >= optionsJson.size()) {
                        System.err.println("Índice da resposta correta fora do intervalo: " + correct);
                        continue;
                    }

                    ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
                    for (Object opt : optionsJson) {
                        if (opt != null) {
                            options.addToRear(opt.toString());
                        }
                    }

                    riddles.addToRear(new Riddle(question, options, correct));
                    riddleCount++;
                } catch (Exception e) {
                    System.err.println("Erro ao processar enigma individual: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar enigmas: " + e.getMessage());
            throw new InvalidJsonException("Erro ao carregar enigmas de " + fileName + ": " + e.getMessage());
        }
        return riddles;
    }
}