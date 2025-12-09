package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTypesTest {

    @Test
    void testEntrance() {
        Entrance e = new Entrance("E1", "Desc Entrada");
        assertEquals("E1", e.getId());
        assertEquals("Desc Entrada", e.getDescription());
        assertDoesNotThrow(e::onEnter);
    }

    @Test
    void testCenter() {
        Center c = new Center("C1", "Sala do Tesouro");
        assertDoesNotThrow(c::onEnter);
    }

    @Test
    void testLeverRoomLogicAndOnEnter() {
        LeverRoom leverRoom = new LeverRoom("L1", "Sala das Alavancas");

        assertDoesNotThrow(leverRoom::onEnter);

        assertFalse(leverRoom.isActivated(), "Alavanca deve começar desativada");

        LeverRoom.LeverResult result = leverRoom.pullLever();

        assertTrue(leverRoom.isActivated(), "Alavanca deve ficar ativada após puxar");
        assertNotNull(result, "O resultado não deve ser nulo");

        assertDoesNotThrow(leverRoom::onEnter);

        assertEquals(LeverRoom.LeverResult.NOTHING, leverRoom.pullLever(), "Puxar segunda vez não deve fazer nada");
    }

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