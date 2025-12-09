package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModelEntitiesTest {

    // --- Testes de Effect ---
    @Test
    void testEffectValues() {
        assertEquals(20, Effect.HEAL.getValue());
        assertEquals(25, Effect.DAMAGE.getValue());
        assertEquals(15, Effect.BONUS_POWER.getValue());
        assertEquals(-30, Effect.TRAP.getValue());
        assertEquals(0, Effect.SWAP_POSITION.getValue());
        assertEquals(0, Effect.SKIP_TURN.getValue());
    }

    // --- Testes de Item ---
    @Test
    void testItemCreation() {
        // Testa construtor e getters do Item
        Item potion = new Item("Health Potion", Effect.HEAL);

        assertEquals("Health Potion", potion.getName());
        assertEquals(Effect.HEAL, potion.getEffect());
        assertEquals("Health Potion (HEAL)", potion.toString());
    }

    // --- Testes de Corridor ---
    @Test
    void testCorridor() {
        Room r1 = new RoomStandard("A", "Sala A");
        Room r2 = new RoomStandard("B", "Sala B");
        RandomEvent event = new RandomEvent("Evento Teste", null, null);

        Corridor c = new Corridor(r1, r2, 5.0, event);

        assertEquals(r1, c.getSource());
        assertEquals(r2, c.getTarget());
        assertEquals(5.0, c.getWeight());
        assertEquals(event, c.getEvent());

        assertDoesNotThrow(c::triggerEvent);

        assertTrue(c.toString().contains("A --(5.0)--> B"));
        assertTrue(c.toString().contains("[EVENT]"));
    }

    @Test
    void testCorridorNoEvent() {
        Room r1 = new RoomStandard("A", "Sala A");
        Room r2 = new RoomStandard("B", "Sala B");

        Corridor c = new Corridor(r1, r2, 1.0, null);
        assertNull(c.getEvent());
        assertDoesNotThrow(c::triggerEvent);
        assertFalse(c.toString().contains("[EVENT]"));
    }
}