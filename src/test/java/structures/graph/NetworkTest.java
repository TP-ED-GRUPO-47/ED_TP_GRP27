package structures.graph;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Network} class (adjacency matrix implementation).
 * <p>
 * Tests weighted graph operations using matrix representation.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class NetworkTest {

    private Network<String> network;

    @BeforeEach
    void setUp() {
        network = new Network<>();
    }

    /**
     * Tests adding vertices and checking network size.
     */
    @Test
    void testAddVertexAndSize() {
        assertTrue(network.isEmpty(), "Network deve iniciar vazia");
        assertEquals(0, network.size());

        network.addVertex("A");
        network.addVertex("B");

        assertFalse(network.isEmpty());
        assertEquals(2, network.size());
    }

    /**
     * Tests adding weighted edges.
     */
    @Test
    void testAddEdgeWeighted() {
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B", 5.5);

        network.addEdge("B", "C");

        Iterator<String> it = network.iteratorBFS("A");
        assertTrue(it.hasNext());
    }

    /**
     * Tests removing edges from the network.
     */
    @Test
    void testRemoveEdge() {
        network.addVertex("A");
        network.addVertex("B");
        network.addEdge("A", "B", 10.0);

        Iterator<String> itBefore = network.iteratorBFS("A");
        assertEquals("A", itBefore.next());
        assertEquals("B", itBefore.next());

        network.removeEdge("A", "B");

        Iterator<String> itAfter = network.iteratorBFS("A");
        assertEquals("A", itAfter.next());
        assertFalse(itAfter.hasNext(), "B não deve ser alcançável após remover aresta");
    }

    /**
     * Tests automatic capacity expansion with many vertices.
     */
    @Test
    void testExpandCapacity() {
        for (int i = 0; i < 15; i++) {
            network.addVertex("V" + i);
        }
        assertEquals(15, network.size());

        network.addEdge("V0", "V14", 99.9);

        Iterator<String> it = network.iteratorBFS("V0");
        boolean foundV14 = false;
        while(it.hasNext()) {
            if (it.next().equals("V14")) foundV14 = true;
        }
        assertTrue(foundV14);
    }

    /**
     * Tests breadth-first search traversal.
     */
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

    /**
     * Tests BFS with disconnected vertices.
     */
    @Test
    void testBFSDisconnected() {
        network.addVertex("A");
        network.addVertex("B");
        Iterator<String> bfs = network.iteratorBFS("A");
        assertEquals("A", bfs.next());
        assertFalse(bfs.hasNext());
    }

    /**
     * Tests depth-first search traversal.
     */
    @Test
    void testDFS() {
        network.addVertex("A");
        network.addVertex("B");
        network.addEdge("A", "B");

        Iterator<String> dfs = network.iteratorDFS("A");
        assertTrue(dfs.hasNext());
        assertEquals("A", dfs.next());
        assertEquals("B", dfs.next());
    }

    /**
     * Tests shortest path iterator using weights.
     */
    @Test
    void testShortestPathIterator() {
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B", 1.0);
        network.addEdge("B", "C", 1.0);
        network.addEdge("A", "C", 100.0);

        Iterator<String> path = network.iteratorShortestPath("A", "C");

        assertTrue(path.hasNext());
        assertEquals("A", path.next());
        assertEquals("B", path.next());
        assertEquals("C", path.next(), "Deve escolher A->B->C (peso 2) em vez de A->C (peso 100)");
        assertFalse(path.hasNext());
    }

    /**
     * Tests shortest path when no path exists.
     */
    @Test
    void testShortestPathNoPath() {
        network.addVertex("A");
        network.addVertex("B");
        Iterator<String> path = network.iteratorShortestPath("A", "B");
        assertFalse(path.hasNext());
    }

    /**
     * Tests Dijkstra algorithm calculating shortest path weight.
     */
    @Test
    void testDijkstraShortestPathWeight() {
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B", 1.0);
        network.addEdge("B", "C", 2.0);
        network.addEdge("A", "C", 10.0);

        double weight = network.shortestPathWeight("A", "C");
        assertEquals(3.0, weight, 0.001);
    }

    /**
     * Tests Dijkstra when no path exists between vertices.
     */
    @Test
    void testDijkstraNoPath() {
        network.addVertex("A");
        network.addVertex("B");

        double weight = network.shortestPathWeight("A", "B");
        assertEquals(Double.POSITIVE_INFINITY, weight);
    }

    /**
     * Tests Dijkstra with source and target being the same vertex.
     */
    @Test
    void testDijkstraSameVertex() {
        network.addVertex("A");

        double weight = network.shortestPathWeight("A", "A");
        assertEquals(0.0, weight);
    }

    /**
     * Tests behavior with invalid vertex indices.
     */
    @Test
    void testInvalidIndices() {
        Iterator<String> it = network.iteratorBFS("Z");
        assertFalse(it.hasNext());

        assertEquals(Double.POSITIVE_INFINITY, network.shortestPathWeight("A", "Z"));
    }
}