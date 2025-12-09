package structures.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {

    private Network<String> network;

    @BeforeEach
    void setUp() {
        network = new Network<>();
    }

    @Test
    void testAddVertexAndSize() {
        assertTrue(network.isEmpty(), "Network deve iniciar vazia");
        assertEquals(0, network.size());

        network.addVertex("A");
        network.addVertex("B");

        assertFalse(network.isEmpty());
        assertEquals(2, network.size());
    }

    @Test
    void testAddEdgeWeighted() {
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B", 5.5);

        network.addEdge("B", "C");

        Iterator<String> it = network.iteratorBFS("A");
        assertTrue(it.hasNext());
        assertEquals("A", it.next());

        boolean foundB = false;
        boolean foundC = false;

        while(it.hasNext()) {
            String v = it.next();
            if (v.equals("B")) foundB = true;
            if (v.equals("C")) foundC = true;
        }
        assertTrue(foundB, "B deve estar conectado a A");
        assertTrue(foundC, "C deve estar conectado a B (e indiretamente a A)");
    }

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
        assertTrue(foundV14, "V14 deve ser alcançável após expansão");
    }

    @Test
    void testBFS() {
        // A -- B -- C
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
    void testBFSDisconnected() {
        network.addVertex("A");
        network.addVertex("B");

        Iterator<String> bfs = network.iteratorBFS("A");
        assertEquals("A", bfs.next());
        assertFalse(bfs.hasNext());
    }

    @Test
    void testDFS() {
        // A -- B -- C
        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B");
        network.addEdge("B", "C");

        Iterator<String> dfs = network.iteratorDFS("A");

        assertTrue(dfs.hasNext());
        assertEquals("A", dfs.next());
        assertEquals("B", dfs.next());
        assertEquals("C", dfs.next());
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
        assertEquals("C", path.next(), "Deve escolher o caminho direto A->C pois tem menos saltos");
        assertFalse(path.hasNext());
    }

    @Test
    void testShortestPathNoPath() {
        network.addVertex("A");
        network.addVertex("B");

        Iterator<String> path = network.iteratorShortestPath("A", "B");
        assertFalse(path.hasNext());
    }

    @Test
    void testShortestPathWeightStub() {
        network.addVertex("A");
        network.addVertex("B");
        assertEquals(0, network.shortestPathWeight("A", "B"));
    }

    @Test
    void testInvalidIndices() {
        Iterator<String> it = network.iteratorBFS("Z");
        assertFalse(it.hasNext());

        it = network.iteratorDFS("Z");
        assertFalse(it.hasNext());

        it = network.iteratorShortestPath("A", "Z");
        assertFalse(it.hasNext());
    }
}