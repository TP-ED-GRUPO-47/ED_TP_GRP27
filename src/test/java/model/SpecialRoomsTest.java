package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import structures.linear.ArrayUnorderedList;

/**
 * Unit tests for special room types.
 * <p>
 * Tests {@link LeverRoom} and {@link RiddleRoom} functionality.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class SpecialRoomsTest {

    /**
     * Tests LeverRoom functionality and state changes.
     */
    @Test
    void testLeverRoom() {
        LeverRoom room = new LeverRoom("L1", "Alavanca");
        assertFalse(room.isSolved());

        LeverRoom.LeverResult result = room.attemptLever();
        assertNotNull(result);
        
        if (result == LeverRoom.LeverResult.CORRECT_CHOICE) {
            assertTrue(room.isSolved());
            assertEquals(LeverRoom.LeverResult.ALREADY_SOLVED, room.attemptLever());
        } else {
            assertFalse(room.isSolved());
        }
    }

    /**
     * Tests riddle answer verification.
     */
    @Test
    void testRiddleCheck() {
        ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
        options.addToRear("Opção A");
        options.addToRear("Opção B");

        Riddle riddle = new Riddle("Pergunta?", options, 1);

        assertTrue(riddle.checkAnswer(1));
        assertFalse(riddle.checkAnswer(0));
    }
}