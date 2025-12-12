package model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for model entities.
 * <p>
 * Tests {@link Effect}, {@link Corridor}, and {@link RandomEvent}.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class ModelEntitiesTest {

    /**
     * Tests that effect constants have correct values.
     */
    @Test
    void testEffectValues() {
        assertEquals(20, Effect.HEAL.getValue());
        assertEquals(25, Effect.DAMAGE.getValue());
        assertEquals(15, Effect.BONUS_POWER.getValue());
        assertEquals(-30, Effect.TRAP.getValue());
        assertEquals(0, Effect.SWAP_POSITION.getValue());
        assertEquals(0, Effect.SKIP_TURN.getValue());
    }

    /**
     * Tests corridor creation with events and weight.
     */
    @Test
    void testCorridor() {
        Room r1 = new RoomStandard("A", "Sala A");
        Room r2 = new RoomStandard("B", "Sala B");
        RandomEvent event = new RandomEvent("Evento Teste", null);

        Corridor c = new Corridor(r1, r2, 5.0, event);

        assertEquals(r1, c.getSource());
        assertEquals(r2, c.getTarget());
        assertEquals(5.0, c.getWeight());
        assertEquals(event, c.getEvent());

        Player p = new Player("Tester");
        assertDoesNotThrow(() -> c.triggerEvent(p));

        assertTrue(c.toString().contains("A --(5.0)--> B"));
        assertTrue(c.toString().contains("[EVENT]"));
    }

    /**
     * Tests corridor without events.
     */
    @Test
    void testCorridorNoEvent() {
        Room r1 = new RoomStandard("A", "Sala A");
        Room r2 = new RoomStandard("B", "Sala B");

        Corridor c = new Corridor(r1, r2, 1.0, null);
        assertNull(c.getEvent());
        assertDoesNotThrow(() -> c.triggerEvent(null));
        assertFalse(c.toString().contains("[EVENT]"));
    }
}