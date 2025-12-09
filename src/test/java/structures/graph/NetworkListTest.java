package structures.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class NetworkListTest {

    private NetworkList<String> network;

    @BeforeEach
    void setUp() {
        network = new NetworkList<>();
    }

    @Test
    void testAddVertexAndSize() {
        assertTrue(network.isEmpty(), "Grafo deve iniciar vazio");
        assertEquals(0, network.size());

        network.addVertex("A");
        network.addVertex("B");

        assertFalse(network.isEmpty());
        assertEquals(2, network.size());
    }

    @Test
    void testAddEdgeAndNeighbors() {
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B", 5.0);
        network.addEdge("A", "C");

        Iterator<String> it = network.getNeighbors("A");

        assertTrue(it.hasNext());
        String n1 = it.next();
        assertTrue(it.hasNext());
        String n2 = it.next();

        assertTrue(n1.equals("B") || n2.equals("B"));
        assertTrue(n1.equals("C") || n2.equals("C"));
    }

    @Test
    void testExpandCapacity() {
        for (int i = 0; i < 15; i++) {
            network.addVertex("V" + i);
        }

        assertEquals(15, network.size());

        network.addEdge("V0", "V14", 10.0);
        Iterator<String> it = network.getNeighbors("V0");
        assertEquals("V14", it.next());
    }

    @Test
    void testBFS() {
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B");
        network.addEdge("B", "C");

        Iterator<String> bfs = network.iteratorBFS("A");

        assertTrue(bfs.hasNext());
        assertEquals("A", bfs.next());
        assertEquals("B", bfs.next());
        assertEquals("C", bfs.next());
    }

    @Test
    void testDFS() {
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B");
        network.addEdge("B", "C");

        Iterator<String> dfs = network.iteratorDFS("A");

        assertTrue(dfs.hasNext());
        assertEquals("A", dfs.next());
        String next = dfs.next();
        assertNotNull(next);
    }

    @Test
    void testShortestPath() {

        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B", 1.0);
        network.addEdge("B", "C", 1.0);
        network.addEdge("A", "C", 100.0);

        Iterator<String> path = network.iteratorShortestPath("A", "C");

        assertTrue(path.hasNext());
        assertEquals("A", path.next());

        assertEquals("C", path.next());
        assertFalse(path.hasNext());
    }

    @Test
    void testInvalidIndicesAndNulls() {
        Iterator<String> it = network.getNeighbors("Ghost");
        assertFalse(it.hasNext(), "Vizinhos de vértice inexistente deve ser vazio");

        Iterator<String> path = network.iteratorShortestPath("A", "B");
        assertFalse(path.hasNext(), "Caminho em grafo vazio deve ser vazio");
    }

    @Test
    void testToString() {
        network.addVertex("A");
        network.addVertex("B");
        network.addEdge("A", "B", 2.5);

        String str = network.toString();
        assertNotNull(str);
        assertTrue(str.contains("A"));
        assertTrue(str.contains("B"));
        assertTrue(str.contains("2.5"));
    }
}