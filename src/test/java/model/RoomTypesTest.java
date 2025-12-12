package model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for different room types.
 * <p>
 * Tests {@link Entrance}, {@link Center}, {@link LeverRoom}, and {@link RiddleRoom}.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class RoomTypesTest {

    /**
     * Tests Entrance room functionality.
     */
    @Test
    void testEntrance() {
        Entrance e = new Entrance("E1", "Desc Entrada");
        assertEquals("E1", e.getId());
        assertEquals("Desc Entrada", e.getDescription());
        assertDoesNotThrow(e::onEnter);
    }

    /**
     * Tests Center (treasure) room functionality.
     */
    @Test
    void testCenter() {
        Center c = new Center("C1", "Sala do Tesouro");
        assertDoesNotThrow(c::onEnter);
    }

    /**
     * Tests LeverRoom logic and interaction.
     */
    @Test
    void testLeverRoomLogicAndOnEnter() {
        LeverRoom leverRoom = new LeverRoom("L1", "Sala das Alavancas");

        assertDoesNotThrow(leverRoom::onEnter);

        assertFalse(leverRoom.isSolved(), "Alavanca deve começar não resolvida");

        LeverRoom.LeverResult result = leverRoom.attemptLever();
        assertNotNull(result, "O resultado não deve ser nulo");
        
        if (result == LeverRoom.LeverResult.CORRECT_CHOICE) {
            assertTrue(leverRoom.isSolved(), "Alavanca deve estar resolvida após sucesso");
            assertEquals(LeverRoom.LeverResult.ALREADY_SOLVED, leverRoom.attemptLever(), 
                "Tentativa seguinte deve retornar ALREADY_SOLVED");
        }
    }

    /**
     * Tests room equality based on ID.
     */
    @Test
    void testRoomEquality() {
        Room r1 = new RoomStandard("A", "Desc");
        Room r2 = new RoomStandard("A", "Outra Desc");
        Room r3 = new RoomStandard("B", "Desc");

        assertEquals(r1, r2, "Salas com mesmo ID devem ser iguais");
        assertNotEquals(r1, r3, "Salas com IDs diferentes devem ser diferentes");
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}