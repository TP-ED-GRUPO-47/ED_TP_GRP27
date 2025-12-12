package structures.heap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.EmptyCollectionException;

/**
 * Unit tests for the {@link PriorityQueue} class.
 * <p>
 * This test suite validates the priority queue implementation built on top
 * of {@link ArrayHeap}, ensuring elements are ordered by priority (lower
 * numbers = higher priority), with FIFO ordering for equal priorities.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Element insertion with priority values</li>
 *   <li>Removal of highest priority element (removeNext)</li>
 *   <li>Priority ordering (lower priority number = higher priority)</li>
 *   <li>FIFO ordering for equal priorities</li>
 *   <li>Mixed priority operations</li>
 *   <li>Queue properties (size, isEmpty)</li>
 *   <li>Edge cases (empty queue, single element, all equal priorities)</li>
 *   <li>Exception handling for invalid operations</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see PriorityQueue
 * @see PriorityQueueNode
 * @see ArrayHeap
 */
class PriorityQueueTest {

    private PriorityQueue<String> queue;

    /**
     * Sets up a fresh empty priority queue before each test.
     */
    @BeforeEach
    void setUp() {
        queue = new PriorityQueue<>();
    }

    /**
     * Tests creating an empty priority queue.
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
     * Tests adding a single element with priority.
     */
    @Test
    void testAddSingleElement() {
        queue.addElement("Task1", 5);

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
    }

    /**
     * Tests adding and removing a single element.
     */
    @Test
    void testAddRemoveSingleElement() {
        queue.addElement("Task1", 10);

        assertEquals("Task1", queue.removeNext());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    /**
     * Tests priority ordering - lower priority number = higher priority.
     * <p>
     * Element with priority 1 should be removed before priority 5.
     * </p>
     */
    @Test
    void testPriorityOrdering() {
        queue.addElement("Low", 10);
        queue.addElement("High", 1);
        queue.addElement("Medium", 5);

        assertEquals("High", queue.removeNext());
        assertEquals("Medium", queue.removeNext());
        assertEquals("Low", queue.removeNext());
    }

    /**
     * Tests adding elements in priority order (ascending).
     */
    @Test
    void testAddAscendingPriority() {
        queue.addElement("P1", 1);
        queue.addElement("P2", 2);
        queue.addElement("P3", 3);
        queue.addElement("P4", 4);
        queue.addElement("P5", 5);

        assertEquals("P1", queue.removeNext());
        assertEquals("P2", queue.removeNext());
        assertEquals("P3", queue.removeNext());
        assertEquals("P4", queue.removeNext());
        assertEquals("P5", queue.removeNext());
    }

    /**
     * Tests adding elements in priority order (descending).
     */
    @Test
    void testAddDescendingPriority() {
        queue.addElement("P5", 5);
        queue.addElement("P4", 4);
        queue.addElement("P3", 3);
        queue.addElement("P2", 2);
        queue.addElement("P1", 1);

        assertEquals("P1", queue.removeNext());
        assertEquals("P2", queue.removeNext());
        assertEquals("P3", queue.removeNext());
        assertEquals("P4", queue.removeNext());
        assertEquals("P5", queue.removeNext());
    }

    /**
     * Tests FIFO ordering for equal priorities.
     * <p>
     * When priorities are equal, elements should be removed in insertion order.
     * </p>
     */
    @Test
    void testEqualPrioritiesFIFO() {
        queue.addElement("First", 5);
        queue.addElement("Second", 5);
        queue.addElement("Third", 5);

        assertEquals("First", queue.removeNext());
        assertEquals("Second", queue.removeNext());
        assertEquals("Third", queue.removeNext());
    }

    /**
     * Tests mixed priorities with some equal.
     */
    @Test
    void testMixedPrioritiesWithDuplicates() {
        queue.addElement("A", 3);
        queue.addElement("B", 1);
        queue.addElement("C", 3);
        queue.addElement("D", 2);
        queue.addElement("E", 1);

        assertEquals("B", queue.removeNext());
        assertEquals("E", queue.removeNext());
        assertEquals("D", queue.removeNext());
        assertEquals("A", queue.removeNext());
        assertEquals("C", queue.removeNext());
    }

    /**
     * Tests removing from an empty queue.
     * <p>
     * Should throw {@link EmptyCollectionException}.
     * </p>
     */
    @Test
    void testRemoveFromEmptyQueue() {
        assertThrows(EmptyCollectionException.class, () -> queue.removeNext());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(queue.isEmpty());

        queue.addElement("Task", 1);
        assertFalse(queue.isEmpty());

        queue.removeNext();
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests the {@code size} method.
     */
    @Test
    void testSize() {
        assertEquals(0, queue.size());

        queue.addElement("T1", 1);
        assertEquals(1, queue.size());

        queue.addElement("T2", 2);
        queue.addElement("T3", 3);
        assertEquals(3, queue.size());

        queue.removeNext();
        assertEquals(2, queue.size());

        queue.removeNext();
        queue.removeNext();
        assertEquals(0, queue.size());
    }

    /**
     * Tests adding elements with negative priorities.
     */
    @Test
    void testNegativePriorities() {
        queue.addElement("Zero", 0);
        queue.addElement("Negative", -10);
        queue.addElement("Positive", 10);

        assertEquals("Negative", queue.removeNext());
        assertEquals("Zero", queue.removeNext());
        assertEquals("Positive", queue.removeNext());
    }

    /**
     * Tests priority queue with various data types.
     */
    @Test
    void testIntegerElements() {
        PriorityQueue<Integer> intQueue = new PriorityQueue<>();

        intQueue.addElement(100, 3);
        intQueue.addElement(200, 1);
        intQueue.addElement(300, 2);

        assertEquals(200, intQueue.removeNext());
        assertEquals(300, intQueue.removeNext());
        assertEquals(100, intQueue.removeNext());
    }

    /**
     * Tests alternating add and remove operations.
     */
    @Test
    void testAlternatingOperations() {
        queue.addElement("A", 5);
        queue.addElement("B", 1);
        assertEquals("B", queue.removeNext());

        queue.addElement("C", 10);
        queue.addElement("D", 3);
        assertEquals("D", queue.removeNext());

        queue.addElement("E", 2);
        assertEquals("E", queue.removeNext());
        assertEquals("A", queue.removeNext());
        assertEquals("C", queue.removeNext());

        assertTrue(queue.isEmpty());
    }

    /**
     * Tests typical task scheduling scenario.
     */
    @Test
    void testTaskScheduling() {
        queue.addElement("Critical Bug", 1);
        queue.addElement("Feature Request", 5);
        queue.addElement("Security Issue", 1);
        queue.addElement("Documentation", 10);
        queue.addElement("Code Review", 3);

        assertEquals("Critical Bug", queue.removeNext());
        assertEquals("Security Issue", queue.removeNext());
        assertEquals("Code Review", queue.removeNext());
        assertEquals("Feature Request", queue.removeNext());
        assertEquals("Documentation", queue.removeNext());
    }

    /**
     * Tests priority queue with many elements.
     */
    @Test
    void testLargePriorityQueue() {
        for (int i = 0; i < 50; i++) {
            queue.addElement("Task" + i, i % 10);
        }

        assertEquals(50, queue.size());

        int previousPriority = Integer.MIN_VALUE;
        while (!queue.isEmpty()) {
            String element = queue.removeNext();
            assertNotNull(element);
        }

        assertTrue(queue.isEmpty());
    }

    /**
     * Tests adding many elements with same priority.
     */
    @Test
    void testAllSamePriority() {
        queue.addElement("First", 5);
        queue.addElement("Second", 5);
        queue.addElement("Third", 5);
        queue.addElement("Fourth", 5);
        queue.addElement("Fifth", 5);

        assertEquals("First", queue.removeNext());
        assertEquals("Second", queue.removeNext());
        assertEquals("Third", queue.removeNext());
        assertEquals("Fourth", queue.removeNext());
        assertEquals("Fifth", queue.removeNext());
    }

    /**
     * Tests two elements with different priorities.
     */
    @Test
    void testTwoElements() {
        queue.addElement("Low", 10);
        queue.addElement("High", 1);

        assertEquals("High", queue.removeNext());
        assertEquals("Low", queue.removeNext());
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests removing all elements repeatedly.
     */
    @Test
    void testRepeatedOperations() {
        for (int round = 0; round < 3; round++) {
            queue.addElement("A", 3);
            queue.addElement("B", 1);
            queue.addElement("C", 2);

            assertEquals("B", queue.removeNext());
            assertEquals("C", queue.removeNext());
            assertEquals("A", queue.removeNext());
            assertTrue(queue.isEmpty());
        }
    }

    /**
     * Tests priority queue with zero priority.
     */
    @Test
    void testZeroPriority() {
        queue.addElement("Zero", 0);
        queue.addElement("Negative", -5);
        queue.addElement("Positive", 5);

        assertEquals("Negative", queue.removeNext());
        assertEquals("Zero", queue.removeNext());
        assertEquals("Positive", queue.removeNext());
    }

    /**
     * Tests priority range from very low to very high.
     */
    @Test
    void testPriorityRange() {
        queue.addElement("VeryLow", 1000);
        queue.addElement("VeryHigh", -1000);
        queue.addElement("Medium", 0);

        assertEquals("VeryHigh", queue.removeNext());
        assertEquals("Medium", queue.removeNext());
        assertEquals("VeryLow", queue.removeNext());
    }

    /**
     * Tests complex interleaved operations.
     */
    @Test
    void testComplexOperations() {
        queue.addElement("A", 5);
        queue.addElement("B", 2);
        queue.addElement("C", 8);

        assertEquals("B", queue.removeNext());

        queue.addElement("D", 1);
        queue.addElement("E", 5);

        assertEquals("D", queue.removeNext());
        assertEquals("A", queue.removeNext());
        assertEquals("E", queue.removeNext());

        queue.addElement("F", 3);

        assertEquals("F", queue.removeNext());
        assertEquals("C", queue.removeNext());

        assertTrue(queue.isEmpty());
    }

    /**
     * Tests emergency priority handling.
     */
    @Test
    void testEmergencyPriority() {
        queue.addElement("Normal1", 5);
        queue.addElement("Normal2", 5);
        queue.addElement("Emergency", 0);
        queue.addElement("Normal3", 5);

        assertEquals("Emergency", queue.removeNext());
        assertEquals("Normal1", queue.removeNext());
        assertEquals("Normal2", queue.removeNext());
        assertEquals("Normal3", queue.removeNext());
    }

    /**
     * Tests that null elements can be added (wrapped in nodes).
     */
    @Test
    void testNullElement() {
        assertDoesNotThrow(() -> {
            queue.addElement(null, 5);
            assertNull(queue.removeNext());
        });
    }

    /**
     * Tests priority queue behavior similar to regular queue (FIFO).
     * <p>
     * If all elements have the same priority, should behave like FIFO queue.
     * </p>
     */
    @Test
    void testFIFOBehavior() {
        int priority = 10;
        queue.addElement("1st", priority);
        queue.addElement("2nd", priority);
        queue.addElement("3rd", priority);
        queue.addElement("4th", priority);

        assertEquals("1st", queue.removeNext());
        assertEquals("2nd", queue.removeNext());
        assertEquals("3rd", queue.removeNext());
        assertEquals("4th", queue.removeNext());
    }
}
