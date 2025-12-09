package structures.stack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinearNodeTest {

    @Test
    void testNodeOperations() {
        LinearNode<Integer> node1 = new LinearNode<>(10);

        assertEquals(10, node1.getElement());
        assertNull(node1.getNext());

        node1.setElement(20);
        assertEquals(20, node1.getElement());

        LinearNode<Integer> node2 = new LinearNode<>(30);
        node1.setNext(node2);

        assertNotNull(node1.getNext());
        assertEquals(node2, node1.getNext());
        assertEquals(30, node1.getNext().getElement());
    }
}