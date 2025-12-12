package structures.linear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link DoubleNode} class.
 * <p>
 * Tests node operations for doubly-linked structures with forward and backward pointers.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class DoubleNodeTest {

    /**
     * Tests doubly-linked node operations including element storage, next, and previous pointers.
     */
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