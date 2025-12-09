package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerBotTest {

    @Test
    void testPlayerBasics() {
        Player p = new Player("Jogador1");

        assertEquals("Jogador1", p.getName());
        assertEquals(100, p.getPower(), "Power inicial deve ser 100");

        assertFalse(p.skipsTurn());
        p.setSkipNextTurn(true);
        assertTrue(p.skipsTurn());

        p.updatePower(-50);
        assertEquals(50, p.getPower());
        p.updatePower(200);
        assertEquals(250, p.getPower());
        p.updatePower(-500);
        assertEquals(0, p.getPower(), "Power não deve ser negativo");
    }

    @Test
    void testPlayerMovementAndHistory() {
        Player p = new Player("Viajante");
        Room r1 = new RoomStandard("R1", "Start");
        Room r2 = new RoomStandard("R2", "End");

        p.setCurrentRoom(r1);
        assertEquals(r1, p.getCurrentRoom());
        assertTrue(p.getHistoryString().contains("R1"));

        p.setCurrentRoom(r2);
        assertEquals(r2, p.getCurrentRoom());
        assertTrue(p.getHistoryString().contains("R2"));

        assertDoesNotThrow(p::printHistory);

        assertTrue(p.toString().contains("Viajante"));
        assertTrue(p.toString().contains("R2"));
    }

    @Test
    void testBotCreationAndMove() {
        Bot bot = new Bot("BotTest");

        Maze maze = new Maze();
        Room r1 = new RoomStandard("R1", "Origem");
        Room r2 = new RoomStandard("R2", "Destino");
        maze.addRoom(r1);
        maze.addRoom(r2);
        maze.addCorridor("R1", "R2", 1.0);

        bot.setCurrentRoom(r1);

        String targetId = bot.decideMove(maze);

        assertNotNull(targetId);
        assertEquals("R2", targetId);
    }

    @Test
    void testBotDeadEnd() {
        Bot bot = new Bot("TrappedBot");
        Maze maze = new Maze();
        Room r1 = new RoomStandard("R1", "Isolada");
        maze.addRoom(r1);

        bot.setCurrentRoom(r1);

        assertNull(bot.decideMove(maze));
    }

    @Test
    void testBotNotInRoom() {
        Bot bot = new Bot("LostBot");
        Maze maze = new Maze();

        assertNull(bot.decideMove(maze));
    }
}