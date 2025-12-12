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
 * Unit tests for the {@link CircularArrayQueue} class.
 * <p>
 * This test suite validates the circular array-based queue implementation,
 * ensuring FIFO (First In, First Out) behavior with efficient circular
 * buffer management and automatic capacity expansion.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Enqueue and dequeue operations following FIFO principle</li>
 *   <li>First operation (viewing front without removal)</li>
 *   <li>Queue properties (size, isEmpty)</li>
 *   <li>Circular buffer wrap-around behavior</li>
 *   <li>Automatic capacity expansion</li>
 *   <li>Custom initial capacity</li>
 *   <li>Edge cases (empty queue, single element)</li>
 *   <li>Exception handling for invalid operations</li>
 *   <li>String representation</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see CircularArrayQueue
 * @see QueueADT
 */
class CircularArrayQueueTest {

    private CircularArrayQueue<Integer> queue;

    /**
     * Sets up a fresh empty circular array queue before each test.
     */
    @BeforeEach
    void setUp() {
        queue = new CircularArrayQueue<>();
    }

    /**
     * Tests creating an empty queue with default capacity.
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
     * Tests constructor with custom initial capacity.
     */
    @Test
    void testConstructorWithCapacity() {
        CircularArrayQueue<String> customQueue = new CircularArrayQueue<>(20);

        assertTrue(customQueue.isEmpty());
        assertEquals(0, customQueue.size());
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
     * Tests circular buffer wrap-around behavior.
     * <p>
     * After dequeuing elements, new enqueues should wrap around
     * the circular buffer correctly.
     * </p>
     */
    @Test
    void testCircularWrapAround() {
        CircularArrayQueue<Integer> smallQueue = new CircularArrayQueue<>(5);

        for (int i = 1; i <= 5; i++) {
            smallQueue.enqueue(i);
        }

        smallQueue.dequeue();
        smallQueue.dequeue();

        smallQueue.enqueue(6);
        smallQueue.enqueue(7);

        assertEquals(5, smallQueue.size());
        assertEquals(3, smallQueue.first());
    }

    /**
     * Tests automatic capacity expansion.
     * <p>
     * Creates a queue with small initial capacity and adds more elements
     * to verify that the queue automatically expands.
     * </p>
     */
    @Test
    void testCapacityExpansion() {
        CircularArrayQueue<Integer> smallQueue = new CircularArrayQueue<>(3);

        for (int i = 1; i <= 10; i++) {
            smallQueue.enqueue(i);
        }

        assertEquals(10, smallQueue.size());

        assertEquals(1, smallQueue.dequeue());
        assertEquals(2, smallQueue.dequeue());
        assertEquals(8, smallQueue.size());
    }

    /**
     * Tests enqueuing and dequeuing many elements.
     */
    @Test
    void testLargeQueue() {
        for (int i = 0; i < 100; i++) {
            queue.enqueue(i);
        }

        assertEquals(100, queue.size());
        assertEquals(0, queue.first());

        for (int i = 0; i < 50; i++) {
            assertEquals(i, queue.dequeue());
        }

        assertEquals(50, queue.size());
        assertEquals(50, queue.first());
    }

    /**
     * Tests alternating enqueue and dequeue operations.
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
        CircularArrayQueue<String> stringQueue = new CircularArrayQueue<>();

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
     * Tests capacity expansion with wrap-around.
     * <p>
     * Ensures that capacity expansion works correctly even when
     * the circular buffer has wrapped around.
     * </p>
     */
    @Test
    void testExpansionWithWrapAround() {
        CircularArrayQueue<Integer> smallQueue = new CircularArrayQueue<>(4);

        smallQueue.enqueue(1);
        smallQueue.enqueue(2);
        smallQueue.enqueue(3);
        smallQueue.dequeue();
        smallQueue.dequeue();

        smallQueue.enqueue(4);
        smallQueue.enqueue(5);
        smallQueue.enqueue(6);
        smallQueue.enqueue(7);

        assertEquals(5, smallQueue.size());
        assertEquals(3, smallQueue.first());

        assertEquals(3, smallQueue.dequeue());
        assertEquals(4, smallQueue.dequeue());
        assertEquals(5, smallQueue.dequeue());
        assertEquals(6, smallQueue.dequeue());
        assertEquals(7, smallQueue.dequeue());
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
}
