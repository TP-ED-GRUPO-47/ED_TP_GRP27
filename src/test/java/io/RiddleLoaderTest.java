package io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.Riddle;
import structures.linear.ArrayUnorderedList;

/**
 * Unit tests for the {@link RiddleLoader} class.
 * <p>
 * Tests the loading of riddles from JSON files.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class RiddleLoaderTest {

    /**
     * Tests successful loading of riddles from a JSON file.
     */
    @Test
    void testLoadRiddlesSuccess() {
        ArrayUnorderedList<Riddle> riddles = RiddleLoader.loadRiddles("test_enigmas.json");

        assertNotNull(riddles);
        assertFalse(riddles.isEmpty(), "A lista de enigmas não devia estar vazia.");
        assertEquals(1, riddles.size(), "Devia haver exatamente 1 enigma.");

        Riddle r = riddles.first();
        assertEquals("Quanto é 1+1?", r.getQuestion());
        assertTrue(r.checkAnswer(1));
    }

    /**
     * Tests that loading a non-existent file returns an empty list.
     */
    @Test
    void testLoadRiddlesFileNotFound() {
        ArrayUnorderedList<Riddle> riddles = RiddleLoader.loadRiddles("ficheiro_inexistente.json");

        assertNotNull(riddles);
        assertTrue(riddles.isEmpty(), "Se o ficheiro não existe, deve retornar lista vazia.");
    }
}