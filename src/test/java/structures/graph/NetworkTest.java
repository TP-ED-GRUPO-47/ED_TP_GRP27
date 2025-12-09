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

        // Adiciona aresta com peso específico
        network.addEdge("A", "B", 5.5);

        // Adiciona aresta sem peso (default 1.0)
        network.addEdge("B", "C");

        Iterator<String> it = network.iteratorBFS("A");
        assertTrue(it.hasNext());
    }

    @Test
    void testRemoveEdge() {
        network.addVertex("A");
        network.addVertex("B");
        network.addEdge("A", "B", 10.0);

        // Verifica conectividade antes
        Iterator<String> itBefore = network.iteratorBFS("A");
        assertEquals("A", itBefore.next());
        assertEquals("B", itBefore.next());

        // Remove aresta (define peso como Infinito)
        network.removeEdge("A", "B");

        // Verifica conectividade depois
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
        assertTrue(foundV14);
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
    void testBFSDisconnected() {
        network.addVertex("A");
        network.addVertex("B");
        Iterator<String> bfs = network.iteratorBFS("A");
        assertEquals("A", bfs.next());
        assertFalse(bfs.hasNext());
    }

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

    @Test
    void testShortestPathIterator() {
        // Teste do iterador (baseado em saltos/BFS)
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
    }

    @Test
    void testShortestPathNoPath() {
        network.addVertex("A");
        network.addVertex("B");
        Iterator<String> path = network.iteratorShortestPath("A", "B");
        assertFalse(path.hasNext());
    }

    // --- CORREÇÃO: Testes Reais do Dijkstra ---

    @Test
    void testDijkstraShortestPathWeight() {
        // Cenário para testar pesos:
        // A --(1.0)--> B --(2.0)--> C  (Total: 3.0)
        // A --(10.0)--> C              (Total: 10.0)

        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");

        network.addEdge("A", "B", 1.0);
        network.addEdge("B", "C", 2.0);
        network.addEdge("A", "C", 10.0);

        // O Dijkstra deve escolher A -> B -> C porque 3.0 < 10.0
        double weight = network.shortestPathWeight("A", "C");
        assertEquals(3.0, weight, 0.001);
    }

    @Test
    void testDijkstraNoPath() {
        network.addVertex("A");
        network.addVertex("B");

        // Sem conexão, deve retornar Infinito
        double weight = network.shortestPathWeight("A", "B");
        assertEquals(Double.POSITIVE_INFINITY, weight);
    }

    @Test
    void testDijkstraSameVertex() {
        network.addVertex("A");

        double weight = network.shortestPathWeight("A", "A");
        assertEquals(0.0, weight);
    }

    @Test
    void testInvalidIndices() {
        Iterator<String> it = network.iteratorBFS("Z");
        assertFalse(it.hasNext());

        assertEquals(Double.POSITIVE_INFINITY, network.shortestPathWeight("A", "Z"));
    }
}