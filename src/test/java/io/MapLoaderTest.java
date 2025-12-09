package io;

import model.*;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class MapLoaderTest {

    @Test
    void testLoadMazeSuccess() {

        Maze maze = MapLoader.loadMaze("test_map.json");

        assertNotNull(maze, "O labirinto não deve ser null.");

        Room entrada = maze.getEntrance();
        assertNotNull(entrada, "Deve existir uma entrada (E1).");
        assertTrue(entrada instanceof Entrance);
        assertEquals("E1", entrada.getId());

        Room tesouro = maze.getTreasureRoom();
        assertNotNull(tesouro, "Deve existir um tesouro (C1).");
        assertTrue(tesouro instanceof Center);


        Iterator<Room> neighbors = maze.getNeighbors(entrada);
        assertTrue(neighbors.hasNext());
        Room s1 = neighbors.next();
        assertEquals("S1", s1.getId());
        assertTrue(s1 instanceof RoomStandard);

        Corridor c = maze.getCorridorBetween(entrada, s1);
        assertNotNull(c);
        assertEquals(2.0, c.getWeight());

        RandomEvent evt = c.getEvent();
        assertNotNull(evt, "O corredor E1->S1 devia ter um evento.");
        assertEquals(Effect.HEAL, evt.getDirectEffect());
        assertNotNull(evt.getItem());
        assertEquals("Pocao", evt.getItem().getName());
    }

    @Test
    void testLoadMazeFileNotFound() {
        Maze maze = MapLoader.loadMaze("mapa_fantasma.json");
        assertNotNull(maze);
        assertNull(maze.getEntrance(), "Maze vazio não deve ter entrada.");
    }
}