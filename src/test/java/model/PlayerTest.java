package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPowerUpdates() {
        Player p = new Player("Hero");
        assertEquals(100, p.getPower());

        p.updatePower(-20);
        assertEquals(80, p.getPower());

        p.updatePower(50);
        assertEquals(130, p.getPower()); // Acumula

        p.updatePower(-200);
        assertEquals(0, p.getPower(), "Power não deve ser negativo");
    }

    @Test
    void testSkipTurn() {
        Player p = new Player("Stunned");
        assertFalse(p.skipsTurn());

        p.setSkipNextTurn(true);
        assertTrue(p.skipsTurn());
    }

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