package model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import structures.linear.ArrayUnorderedList;

/**
 * Unit tests for game entities and room types.
 * <p>
 * Tests riddle logic, room equals/hashCode, and random events.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class EntitiesAndRoomsTest {

    /**
     * Tests riddle creation and answer checking logic.
     */
    @Test
    void testRiddleLogic() {
        ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
        options.addToRear("Blue");
        options.addToRear("Red");
        options.addToRear("Green");

        Riddle riddle = new Riddle("What color is fire?", options, 1);

        assertEquals("What color is fire?", riddle.getQuestion());
        assertEquals(options, riddle.getOptions());

        assertTrue(riddle.checkAnswer(1), "Index 1 should be correct");
        assertFalse(riddle.checkAnswer(0), "Index 0 should be incorrect");
        assertFalse(riddle.checkAnswer(2), "Index 2 should be incorrect");
    }

    /**
     * Tests random event with a direct effect.
     */
    @Test
    void testRandomEventFull() {
        RandomEvent event = new RandomEvent("You found a chest!", Effect.HEAL);

        assertEquals(Effect.HEAL, event.getDirectEffect());

        assertDoesNotThrow(() -> event.trigger(new Player("Tester")));
    }

    /**
     * Tests random event with null effect.
     */
    @Test
    void testRandomEventNulls() {
        RandomEvent event = new RandomEvent("Empty wind...", null);

        assertNull(event.getDirectEffect());
        assertDoesNotThrow(() -> event.trigger(null));
    }

    /**
     * Tests standard room creation, equality, and basic operations.
     */
    @Test
    void testRoomStandardAndBaseLogic() {
        Room r1 = new RoomStandard("S1", "Standard Room");
        Room r2 = new RoomStandard("S1", "Same ID Room");
        Room r3 = new RoomStandard("S2", "Different Room");

        assertEquals("S1", r1.getId());
        assertEquals("Standard Room", r1.getDescription());

        assertDoesNotThrow(r1::onEnter);

        assertEquals(r1, r2, "Rooms with same ID should be equal");
        assertNotEquals(r1, r3, "Rooms with different ID should not be equal");
        assertEquals(r1.hashCode(), r2.hashCode());

        assertTrue(r1.toString().contains("S1"));
    }

    /**
     * Tests riddle room initialization and puzzle interaction.
     */
    @Test
    void testRiddleRoomLogic() {
        Riddle r = new Riddle("Q", new ArrayUnorderedList<>(), 0);
        RiddleRoom room = new RiddleRoom("R1", "Puzzle Room", r);

        assertFalse(room.isSolved());
        assertEquals(r, room.getRiddle());

        assertDoesNotThrow(room::onEnter);

        room.setSolved(true);
        assertTrue(room.isSolved());

        assertDoesNotThrow(room::onEnter);
    }
}