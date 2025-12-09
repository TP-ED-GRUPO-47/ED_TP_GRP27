package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class MazeTest {

    private Maze maze;

    @BeforeEach
    void setUp() {
        maze = new Maze();
    }

    @Test
    void testAddRoomAndCorridor() {
        Room r1 = new RoomStandard("A", "A");
        Room r2 = new RoomStandard("B", "B");

        maze.addRoom(r1);
        maze.addRoom(r2);

        maze.addCorridor("A", "B", 5.0);

        Corridor c = maze.getCorridorBetween(r1, r2);
        assertNotNull(c);
        assertEquals(5.0, c.getWeight());

        Iterator<Room> neighbors = maze.getNeighbors(r1);
        assertTrue(neighbors.hasNext());
        assertEquals(r2, neighbors.next());
    }

    @Test
    void testGetEntranceAndTreasure() {
        assertNull(maze.getEntrance());
        assertNull(maze.getTreasureRoom());

        Room e = new Entrance("E1", "Entrada");
        Room c = new Center("C1", "Centro");

        maze.addRoom(e);
        maze.addRoom(c);

        assertEquals(e, maze.getEntrance());
        assertEquals(c, maze.getTreasureRoom());
    }

    @Test
    void testSecretPassage() {
        Room r1 = new RoomStandard("A", "A");
        Room r2 = new RoomStandard("B", "B");
        Room r3 = new RoomStandard("C", "C");

        maze.addRoom(r1);
        maze.addRoom(r2);
        maze.addRoom(r3);

        maze.addCorridor("A", "B", 1.0);

        String targetId = maze.createSecretPassage(r1);

        assertNotNull(targetId);
        assertNotEquals("A", targetId);

        Room targetRoom = targetId.equals("B") ? r2 : r3;
        assertNotNull(maze.getCorridorBetween(r1, targetRoom));
    }

    @Test
    void testExitsString() {
        Room r1 = new RoomStandard("A", "A");
        assertEquals("Nenhuma", maze.getAvailableExits(null));
        assertEquals("Sem saídas (Beco sem saída)", maze.getAvailableExits(r1));
    }
}