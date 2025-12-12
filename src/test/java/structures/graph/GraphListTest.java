package structures.graph;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link GraphList} class.
 * <p>
 * Tests unweighted graph operations using adjacency list representation.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class GraphListTest {

    private GraphList<String> graph;

    @BeforeEach
    void setUp() {
        graph = new GraphList<>();
    }

    /**
     * Tests adding vertices and checking graph size.
     */
    @Test
    void testAddVertexAndSize() {
        assertTrue(graph.isEmpty(), "Grafo deve iniciar vazio");
        assertEquals(0, graph.size());

        graph.addVertex("A");
        graph.addVertex("B");

        assertFalse(graph.isEmpty());
        assertEquals(2, graph.size());
    }

    /**
     * Tests automatic capacity expansion when adding many vertices.
     */
    @Test
    void testExpandCapacity() {
        for (int i = 0; i < 15; i++) {
            graph.addVertex("V" + i);
        }

        assertEquals(15, graph.size());

        graph.addEdge("V0", "V14");

        Iterator<String> it = graph.iteratorBFS("V0");
        boolean foundV14 = false;
        while(it.hasNext()) {
            if (it.next().equals("V14")) foundV14 = true;
        }
        assertTrue(foundV14, "V14 deve ser alcançável a partir de V0 após expansão");
    }

    /**
     * Tests adding edges and breadth-first search traversal.
     */
    @Test
    void testAddEdgeAndBFS() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        Iterator<String> bfs = graph.iteratorBFS("A");

        assertTrue(bfs.hasNext());
        assertEquals("A", bfs.next());
        assertEquals("B", bfs.next());
        assertEquals("C", bfs.next());
    }

    /**
     * Tests depth-first search traversal.
     */
    @Test
    void testDFS() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        Iterator<String> dfs = graph.iteratorDFS("A");

        assertTrue(dfs.hasNext());
        assertEquals("A", dfs.next());
        assertEquals("B", dfs.next());
        assertEquals("C", dfs.next());
    }

    /**
     * Tests DFS with branching paths.
     */
    @Test
    void testDFSBranching() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");

        Iterator<String> dfs = graph.iteratorDFS("A");
        assertEquals("A", dfs.next());

        String second = dfs.next();
        String third = dfs.next();

        assertTrue(second.equals("B") || second.equals("C"));
        assertTrue(third.equals("B") || third.equals("C"));
    }

    /**
     * Tests finding the shortest path between two vertices.
     */
    @Test
    void testShortestPath() {

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "D");

        Iterator<String> path = graph.iteratorShortestPath("A", "C");

        assertTrue(path.hasNext());
        assertEquals("A", path.next());
        assertEquals("B", path.next());
        assertEquals("C", path.next());
        assertFalse(path.hasNext());
    }

    /**
     * Tests shortest path when source and target are the same vertex.
     */
    @Test
    void testShortestPathSameVertex() {
        graph.addVertex("A");
        Iterator<String> path = graph.iteratorShortestPath("A", "A");

        assertTrue(path.hasNext());
        assertEquals("A", path.next());
        assertFalse(path.hasNext());
    }

    /**
     * Tests that no path is returned when vertices are disconnected.
     */
    @Test
    void testNoPath() {
        graph.addVertex("A");
        graph.addVertex("B");

        Iterator<String> path = graph.iteratorShortestPath("A", "B");
        assertFalse(path.hasNext());
    }

    /**
     * Tests behavior with invalid vertices.
     */
    @Test
    void testInvalidOperations() {
        Iterator<String> it = graph.iteratorBFS("Z");
        assertFalse(it.hasNext());

        it = graph.iteratorDFS("Z");
        assertFalse(it.hasNext());

        it = graph.iteratorShortestPath("A", "Z");
        assertFalse(it.hasNext());
    }

    /**
     * Tests stub methods not fully implemented.
     */
    @Test
    void testStubs() {
        graph.addVertex("A");

        assertDoesNotThrow(() -> graph.removeVertex("A"));
        assertDoesNotThrow(() -> graph.removeEdge("A", "B"));

        assertFalse(graph.isConnected(), "isConnected está hardcoded para false");
    }

    /**
     * Tests string representation of the graph.
     */
    @Test
    void testToString() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        String str = graph.toString();
        assertNotNull(str);
        assertTrue(str.contains("A"));
        assertTrue(str.contains("B"));
    }
}