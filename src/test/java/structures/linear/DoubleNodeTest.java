package structures.linear;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoubleNodeTest {

    @Test
    void testNodeOperations() {
        DoubleNode<String> node1 = new DoubleNode<>("A");
        DoubleNode<String> node2 = new DoubleNode<>("B");

        assertEquals("A", node1.getElement());
        node1.setElement("C");
        assertEquals("C", node1.getElement());

        assertNull(node1.getNext());
        node1.setNext(node2);
        assertEquals(node2, node1.getNext());

        assertNull(node2.getPrevious());
        node2.setPrevious(node1);
        assertEquals(node1, node2.getPrevious());
    }
}