package model;

import org.junit.jupiter.api.Test;
import structures.linear.ArrayUnorderedList;
import static org.junit.jupiter.api.Assertions.*;

class SpecialRoomsTest {

    @Test
    void testLeverRoom() {
        LeverRoom room = new LeverRoom("L1", "Alavanca");
        assertFalse(room.isActivated());

        LeverRoom.LeverResult result = room.pullLever();
        assertTrue(room.isActivated());
        assertNotNull(result);

        assertEquals(LeverRoom.LeverResult.NOTHING, room.pullLever());
    }

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