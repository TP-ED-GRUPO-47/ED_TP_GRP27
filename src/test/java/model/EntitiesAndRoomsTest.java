package model;

import org.junit.jupiter.api.Test;
import structures.linear.ArrayUnorderedList;
import static org.junit.jupiter.api.Assertions.*;

class EntitiesAndRoomsTest {

    // --- RIDDLE TESTS ---
    @Test
    void testRiddleLogic() {
        // Prepare options
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

    // --- RANDOM EVENT TESTS ---
    @Test
    void testRandomEventFull() {
        Item item = new Item("Gold Coin", Effect.BONUS_POWER);
        RandomEvent event = new RandomEvent("You found a chest!", item, Effect.HEAL);

        assertEquals(Effect.HEAL, event.getDirectEffect());
        assertEquals(item, event.getItem());

        assertDoesNotThrow(event::trigger);
    }

    @Test
    void testRandomEventNulls() {
        RandomEvent event = new RandomEvent("Empty wind...", null, null);

        assertNull(event.getItem());
        assertNull(event.getDirectEffect());
        assertDoesNotThrow(event::trigger);
    }

    // --- ROOM & ROOM STANDARD TESTS ---
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

    // --- RIDDLE ROOM TESTS ---
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