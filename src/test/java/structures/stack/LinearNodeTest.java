package structures.stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link LinearNode} class.
 * <p>
 * Tests node operations for singly-linked structures.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class LinearNodeTest {

    /**
     * Tests basic node operations including element storage and next pointer management.
     */
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