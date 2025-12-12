package structures.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link QueueStacks} class.
 * <p>
 * This test suite validates the queue implementation backed by two stacks,
 * ensuring FIFO (First In, First Out) behavior with amortized constant-time
 * operations through lazy stack shifting.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Enqueue and dequeue operations following FIFO principle</li>
 *   <li>First operation (viewing front without removal)</li>
 *   <li>Queue properties (size, isEmpty)</li>
 *   <li>Stack shifting mechanism (lazy transfer between stacks)</li>
 *   <li>Alternating operations to test stack switching</li>
 *   <li>Edge cases (empty queue, single element)</li>
 *   <li>Exception handling for invalid operations</li>
 *   <li>String representation</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see QueueStacks
 * @see QueueADT
 */
class QueueStacksTest {

    private QueueStacks<Integer> queue;

    /**
     * Sets up a fresh empty queue backed by stacks before each test.
     */
    @BeforeEach
    void setUp() {
        queue = new QueueStacks<>();
    }

    /**
     * Tests creating an empty queue.
     * <p>
     * Verifies initial state with no elements.
     * </p>
     */
    @Test
    void testEmptyQueueCreation() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    /**
     * Tests enqueuing a single element.
     */
    @Test
    void testEnqueueSingleElement() {
        queue.enqueue(10);

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals(10, queue.first());
    }

    /**
     * Tests enqueuing multiple elements.
     * <p>
     * Verifies FIFO behavior - first enqueued element should be at front.
     * </p>
     */
    @Test
    void testEnqueueMultipleElements() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertEquals(3, queue.size());
        assertEquals(10, queue.first());
        assertFalse(queue.isEmpty());
    }

    /**
     * Tests dequeuing an element from the queue.
     * <p>
     * Verifies that dequeue returns and removes the front element.
     * </p>
     */
    @Test
    void testDequeue() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertEquals(10, queue.dequeue());
        assertEquals(2, queue.size());
        assertEquals(20, queue.first());

        assertEquals(20, queue.dequeue());
        assertEquals(1, queue.size());
        assertEquals(30, queue.first());
    }

    /**
     * Tests FIFO (First In, First Out) behavior.
     */
    @Test
    void testFIFOBehavior() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);

        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests first operation.
     * <p>
     * Verifies that first returns the front element without removing it.
     * </p>
     */
    @Test
    void testFirst() {
        queue.enqueue(10);
        queue.enqueue(20);

        assertEquals(10, queue.first());
        assertEquals(2, queue.size());
        assertEquals(10, queue.first());
    }

    /**
     * Tests dequeuing from an empty queue.
     * <p>
     * Should throw {@link IllegalStateException}.
     * </p>
     */
    @Test
    void testDequeueFromEmptyQueue() {
        assertThrows(IllegalStateException.class, () -> queue.dequeue());
    }

    /**
     * Tests first on an empty queue.
     * <p>
     * Should throw {@link IllegalStateException}.
     * </p>
     */
    @Test
    void testFirstOnEmptyQueue() {
        assertThrows(IllegalStateException.class, () -> queue.first());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(queue.isEmpty());

        queue.enqueue(10);
        assertFalse(queue.isEmpty());

        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests the {@code size} method.
     */
    @Test
    void testSize() {
        assertEquals(0, queue.size());

        queue.enqueue(10);
        assertEquals(1, queue.size());

        queue.enqueue(20);
        queue.enqueue(30);
        assertEquals(3, queue.size());

        queue.dequeue();
        assertEquals(2, queue.size());

        queue.dequeue();
        queue.dequeue();
        assertEquals(0, queue.size());
    }

    /**
     * Tests stack shifting mechanism.
     * <p>
     * After elements are enqueued, the first dequeue should trigger
     * a shift from inStack to outStack, reversing the order.
     * </p>
     */
    @Test
    void testStackShifting() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.first());
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());

        queue.enqueue(4);
        queue.enqueue(5);

        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertEquals(5, queue.dequeue());
    }

    /**
     * Tests alternating enqueue and dequeue operations.
     * <p>
     * This tests the efficiency of the two-stack approach with
     * interleaved operations.
     * </p>
     */
    @Test
    void testAlternatingOperations() {
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(1, queue.dequeue());

        queue.enqueue(3);
        queue.enqueue(4);
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());

        queue.enqueue(5);
        assertEquals(4, queue.first());
        assertEquals(4, queue.dequeue());
        assertEquals(5, queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    /**
     * Tests dequeuing all elements until empty.
     */
    @Test
    void testDequeueAllElements() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());

        assertThrows(IllegalStateException.class, () -> queue.dequeue());
    }

    /**
     * Tests queue with duplicate elements.
     */
    @Test
    void testDuplicateElements() {
        queue.enqueue(5);
        queue.enqueue(5);
        queue.enqueue(5);

        assertEquals(3, queue.size());
        assertEquals(5, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests queue with null elements.
     */
    @Test
    void testNullElements() {
        queue.enqueue(null);
        queue.enqueue(10);

        assertEquals(2, queue.size());
        assertNull(queue.dequeue());
        assertEquals(10, queue.dequeue());
    }

    /**
     * Tests queue with negative numbers.
     */
    @Test
    void testNegativeNumbers() {
        queue.enqueue(-10);
        queue.enqueue(-20);
        queue.enqueue(0);
        queue.enqueue(10);

        assertEquals(-10, queue.dequeue());
        assertEquals(-20, queue.dequeue());
        assertEquals(0, queue.dequeue());
        assertEquals(10, queue.dequeue());
    }

    /**
     * Tests queue with String elements.
     */
    @Test
    void testStringElements() {
        QueueStacks<String> stringQueue = new QueueStacks<>();

        stringQueue.enqueue("First");
        stringQueue.enqueue("Second");
        stringQueue.enqueue("Third");

        assertEquals("First", stringQueue.dequeue());
        assertEquals("Second", stringQueue.first());
        assertEquals(2, stringQueue.size());
    }

    /**
     * Tests the {@code toString} method.
     */
    @Test
    void testToString() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        String result = queue.toString();
        assertNotNull(result);
        assertTrue(result.contains("size"));
    }

    /**
     * Tests first after dequeue.
     */
    @Test
    void testFirstAfterDequeue() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        queue.dequeue();
        assertEquals(20, queue.first());

        queue.dequeue();
        assertEquals(30, queue.first());
    }

    /**
     * Tests single element queue operations.
     */
    @Test
    void testSingleElementQueue() {
        queue.enqueue(42);

        assertEquals(42, queue.first());
        assertEquals(1, queue.size());
        assertFalse(queue.isEmpty());

        assertEquals(42, queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    /**
     * Tests queue behavior with extreme values.
     */
    @Test
    void testExtremeValues() {
        queue.enqueue(Integer.MAX_VALUE);
        queue.enqueue(Integer.MIN_VALUE);
        queue.enqueue(0);

        assertEquals(Integer.MAX_VALUE, queue.dequeue());
        assertEquals(Integer.MIN_VALUE, queue.dequeue());
        assertEquals(0, queue.dequeue());
    }

    /**
     * Tests multiple first operations without modifying queue.
     */
    @Test
    void testMultipleFirstCalls() {
        queue.enqueue(100);

        for (int i = 0; i < 10; i++) {
            assertEquals(100, queue.first());
            assertEquals(1, queue.size());
        }

        assertEquals(100, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests queue after clearing all elements.
     */
    @Test
    void testQueueAfterClearing() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        assertTrue(queue.isEmpty());

        queue.enqueue(10);
        assertEquals(1, queue.size());
        assertEquals(10, queue.first());
    }

    /**
     * Tests exception message for empty queue operations.
     */
    @Test
    void testExceptionMessages() {
        IllegalStateException dequeueException = 
            assertThrows(IllegalStateException.class, () -> queue.dequeue());
        assertTrue(dequeueException.getMessage().contains("empty"));

        IllegalStateException firstException = 
            assertThrows(IllegalStateException.class, () -> queue.first());
        assertTrue(firstException.getMessage().contains("empty"));
    }

    /**
     * Tests sequential enqueue and dequeue operations.
     */
    @Test
    void testSequentialOperations() {
        for (int i = 0; i < 50; i++) {
            queue.enqueue(i);
            assertEquals(i + 1, queue.size());
        }

        for (int i = 0; i < 50; i++) {
            assertEquals(i, queue.dequeue());
            assertEquals(49 - i, queue.size());
        }

        assertTrue(queue.isEmpty());
    }

    /**
     * Tests that operations maintain queue integrity.
     */
    @Test
    void testQueueIntegrity() {
        queue.enqueue(1);
        queue.enqueue(2);
        int first1 = queue.dequeue();
        queue.enqueue(3);
        int first2 = queue.first();
        int first3 = queue.dequeue();
        int first4 = queue.dequeue();

        assertEquals(1, first1);
        assertEquals(2, first2);
        assertEquals(2, first3);
        assertEquals(3, first4);
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests enqueue and dequeue in batches.
     */
    @Test
    void testBatchOperations() {
        for (int i = 1; i <= 20; i++) {
            queue.enqueue(i);
        }
        assertEquals(20, queue.size());

        for (int i = 1; i <= 10; i++) {
            assertEquals(i, queue.dequeue());
        }
        assertEquals(10, queue.size());

        for (int i = 21; i <= 30; i++) {
            queue.enqueue(i);
        }
        assertEquals(20, queue.size());

        assertEquals(11, queue.first());
    }

    /**
     * Tests lazy shifting behavior.
     * <p>
     * Multiple enqueues followed by a single first() should trigger
     * one shift, then subsequent operations use the shifted state.
     * </p>
     */
    @Test
    void testLazyShifting() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);

        assertEquals(1, queue.first());

        assertEquals(1, queue.first());
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.first());
    }

    /**
     * Tests large number of operations.
     */
    @Test
    void testLargeOperations() {
        for (int i = 0; i < 100; i++) {
            queue.enqueue(i);
        }

        assertEquals(100, queue.size());

        for (int i = 0; i < 50; i++) {
            assertEquals(i, queue.dequeue());
        }

        assertEquals(50, queue.size());
        assertEquals(50, queue.first());
    }

    /**
     * Tests mixed operations pattern.
     */
    @Test
    void testMixedPattern() {
        queue.enqueue(1);
        assertEquals(1, queue.first());
        
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.dequeue());
        
        queue.enqueue(4);
        assertEquals(2, queue.first());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        
        queue.enqueue(5);
        queue.enqueue(6);
        assertEquals(4, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertEquals(6, queue.dequeue());
        
        assertTrue(queue.isEmpty());
    }
}
