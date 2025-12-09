package structures.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class GraphListTest {

    private GraphList<String> graph;

    @BeforeEach
    void setUp() {
        graph = new GraphList<>();
    }

    @Test
    void testAddVertexAndSize() {
        assertTrue(graph.isEmpty(), "Grafo deve iniciar vazio");
        assertEquals(0, graph.size());

        graph.addVertex("A");
        graph.addVertex("B");

        assertFalse(graph.isEmpty());
        assertEquals(2, graph.size());
    }

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

    @Test
    void testAddEdgeAndBFS() {
        // Grafo: A -- B -- C
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

    @Test
    void testShortestPathSameVertex() {
        graph.addVertex("A");
        Iterator<String> path = graph.iteratorShortestPath("A", "A");

        assertTrue(path.hasNext());
        assertEquals("A", path.next());
        assertFalse(path.hasNext());
    }

    @Test
    void testNoPath() {
        graph.addVertex("A");
        graph.addVertex("B");

        Iterator<String> path = graph.iteratorShortestPath("A", "B");
        assertFalse(path.hasNext());
    }

    @Test
    void testInvalidOperations() {
        Iterator<String> it = graph.iteratorBFS("Z");
        assertFalse(it.hasNext());

        it = graph.iteratorDFS("Z");
        assertFalse(it.hasNext());

        it = graph.iteratorShortestPath("A", "Z"); // A existe, Z não
        assertFalse(it.hasNext());
    }

    @Test
    void testStubs() {
        graph.addVertex("A");

        assertDoesNotThrow(() -> graph.removeVertex("A"));
        assertDoesNotThrow(() -> graph.removeEdge("A", "B"));

        assertFalse(graph.isConnected(), "isConnected está hardcoded para false");
    }

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