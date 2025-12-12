package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Player} class.
 * <p>
 * Tests player attributes like power, turn skipping, and movement history.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class PlayerTest {

    /**
     * Tests that player power updates correctly, including negative values.
     */
    @Test
    void testPowerUpdates() {
        Player p = new Player("Hero");
        assertEquals(100, p.getPower());

        p.updatePower(-20);
        assertEquals(80, p.getPower());

        p.updatePower(50);
        assertEquals(130, p.getPower());

        p.updatePower(-200);
        assertEquals(0, p.getPower(), "Power não deve ser negativo");
    }

    /**
     * Tests the skip turn mechanism for players.
     */
    @Test
    void testSkipTurn() {
        Player p = new Player("Stunned");
        assertFalse(p.skipsTurn());

        p.setSkipNextTurn(true);
        assertTrue(p.skipsTurn());
    }

    /**
     * Tests that player movement history is correctly tracked.
     */
    @Test
    void testHistory() {
        Player p = new Player("Traveler");
        Room r1 = new RoomStandard("R1", "Sala 1");
        Room r2 = new RoomStandard("R2", "Sala 2");

        p.setCurrentRoom(r1);
        p.setCurrentRoom(r2);

        String history = p.getHistoryString();
        assertTrue(history.contains("R1"));
        assertTrue(history.contains("R2"));
    }
}