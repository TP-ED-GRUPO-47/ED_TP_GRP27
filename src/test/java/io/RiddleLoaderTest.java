package io;

import model.Riddle;
import org.junit.jupiter.api.Test;
import structures.linear.ArrayUnorderedList;
import static org.junit.jupiter.api.Assertions.*;

class RiddleLoaderTest {

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

    @Test
    void testLoadRiddlesFileNotFound() {
        ArrayUnorderedList<Riddle> riddles = RiddleLoader.loadRiddles("ficheiro_inexistente.json");

        assertNotNull(riddles);
        assertTrue(riddles.isEmpty(), "Se o ficheiro não existe, deve retornar lista vazia.");
    }
}