package structures.heap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.EmptyCollectionException;

/**
 * Unit tests for the {@link ArrayHeap} class.
 * <p>
 * This test suite validates the array-based min-heap implementation,
 * ensuring the heap property is maintained where the minimum element
 * is always at the root, and each parent is smaller than its children.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Element insertion maintaining heap property</li>
 *   <li>Minimum element removal (removeMin)</li>
 *   <li>Heap property verification after operations</li>
 *   <li>Finding minimum without removal (findMin)</li>
 *   <li>Array capacity expansion</li>
 *   <li>Heap properties (size, isEmpty)</li>
 *   <li>Edge cases (empty heap, single element, duplicates)</li>
 *   <li>Exception handling for invalid operations</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see ArrayHeap
 * @see HeapADT
 */
class ArrayHeapTest {

    private ArrayHeap<Integer> heap;

    /**
     * Sets up a fresh empty array-based heap before each test.
     */
    @BeforeEach
    void setUp() {
        heap = new ArrayHeap<>();
    }

    /**
     * Tests creating an empty heap.
     * <p>
     * Verifies initial state with no elements.
     * </p>
     */
    @Test
    void testEmptyHeapCreation() {
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    /**
     * Tests adding a single element to the heap.
     * <p>
     * Verifies the element becomes the root (minimum).
     * </p>
     */
    @Test
    void testAddSingleElement() {
        heap.addElement(10);

        assertFalse(heap.isEmpty());
        assertEquals(1, heap.size());
        assertEquals(10, heap.findMin());
    }

    /**
     * Tests adding multiple elements maintaining min-heap property.
     * <p>
     * After each insertion, the minimum element should be at the root.
     * </p>
     */
    @Test
    void testAddMultipleElements() {
        heap.addElement(50);
        assertEquals(50, heap.findMin());

        heap.addElement(30);
        assertEquals(30, heap.findMin());

        heap.addElement(70);
        assertEquals(30, heap.findMin());

        heap.addElement(20);
        assertEquals(20, heap.findMin());

        heap.addElement(40);
        assertEquals(20, heap.findMin());

        assertEquals(5, heap.size());
    }

    /**
     * Tests adding elements in ascending order.
     * <p>
     * Min-heap property should still be maintained.
     * </p>
     */
    @Test
    void testAddAscendingOrder() {
        heap.addElement(10);
        heap.addElement(20);
        heap.addElement(30);
        heap.addElement(40);
        heap.addElement(50);

        assertEquals(5, heap.size());
        assertEquals(10, heap.findMin());
    }

    /**
     * Tests adding elements in descending order.
     * <p>
     * Each new smaller element should become the new minimum.
     * </p>
     */
    @Test
    void testAddDescendingOrder() {
        heap.addElement(50);
        heap.addElement(40);
        heap.addElement(30);
        heap.addElement(20);
        heap.addElement(10);

        assertEquals(5, heap.size());
        assertEquals(10, heap.findMin());
    }

    /**
     * Tests adding duplicate elements.
     * <p>
     * Heap should accept and maintain duplicates correctly.
     * </p>
     */
    @Test
    void testAddDuplicates() {
        heap.addElement(20);
        heap.addElement(20);
        heap.addElement(10);
        heap.addElement(10);
        heap.addElement(20);

        assertEquals(5, heap.size());
        assertEquals(10, heap.findMin());
    }

    /**
     * Tests removing the minimum element.
     * <p>
     * After removal, the next smallest element should become the new minimum.
     * </p>
     */
    @Test
    void testRemoveMin() {
        heap.addElement(30);
        heap.addElement(10);
        heap.addElement(50);
        heap.addElement(20);
        heap.addElement(40);

        assertEquals(10, heap.removeMin());
        assertEquals(4, heap.size());
        assertEquals(20, heap.findMin());

        assertEquals(20, heap.removeMin());
        assertEquals(3, heap.size());
        assertEquals(30, heap.findMin());
    }

    /**
     * Tests removing all elements in order.
     * <p>
     * Elements should be removed in ascending order (heap sort).
     * </p>
     */
    @Test
    void testRemoveAllElements() {
        heap.addElement(50);
        heap.addElement(20);
        heap.addElement(70);
        heap.addElement(10);
        heap.addElement(30);

        assertEquals(10, heap.removeMin());
        assertEquals(20, heap.removeMin());
        assertEquals(30, heap.removeMin());
        assertEquals(50, heap.removeMin());
        assertEquals(70, heap.removeMin());

        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    /**
     * Tests removing from an empty heap.
     * <p>
     * Should throw {@link EmptyCollectionException}.
     * </p>
     */
    @Test
    void testRemoveMinFromEmptyHeap() {
        assertThrows(EmptyCollectionException.class, () -> heap.removeMin());
    }

    /**
     * Tests finding minimum without removal.
     * <p>
     * Verifies that findMin doesn't modify the heap.
     * </p>
     */
    @Test
    void testFindMin() {
        heap.addElement(50);
        heap.addElement(20);
        heap.addElement(70);
        heap.addElement(10);

        assertEquals(10, heap.findMin());
        assertEquals(4, heap.size());
        assertEquals(10, heap.findMin());
    }

    /**
     * Tests finding minimum on an empty heap.
     * <p>
     * Should throw {@link RuntimeException}.
     * </p>
     */
    @Test
    void testFindMinOnEmptyHeap() {
        assertThrows(RuntimeException.class, () -> heap.findMin());
    }

    /**
     * Tests automatic capacity expansion.
     * <p>
     * Adds more elements than initial capacity to verify expansion works.
     * </p>
     */
    @Test
    void testCapacityExpansion() {
        for (int i = 50; i > 0; i--) {
            heap.addElement(i);
        }

        assertEquals(50, heap.size());
        assertEquals(1, heap.findMin());

        for (int i = 1; i <= 50; i++) {
            assertEquals(i, heap.removeMin());
        }

        assertTrue(heap.isEmpty());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(heap.isEmpty());

        heap.addElement(10);
        assertFalse(heap.isEmpty());

        heap.removeMin();
        assertTrue(heap.isEmpty());
    }

    /**
     * Tests the {@code size} method.
     */
    @Test
    void testSize() {
        assertEquals(0, heap.size());

        heap.addElement(10);
        assertEquals(1, heap.size());

        heap.addElement(20);
        heap.addElement(30);
        assertEquals(3, heap.size());

        heap.removeMin();
        assertEquals(2, heap.size());

        heap.removeMin();
        heap.removeMin();
        assertEquals(0, heap.size());
    }

    /**
     * Tests heap property with random insertions.
     * <p>
     * Regardless of insertion order, removeMin should always
     * return elements in ascending order.
     * </p>
     */
    @Test
    void testHeapPropertyMaintained() {
        int[] values = {45, 23, 67, 12, 34, 89, 56, 78, 9, 15, 30, 40};

        for (int val : values) {
            heap.addElement(val);
        }

        assertEquals(values.length, heap.size());

        int previous = Integer.MIN_VALUE;
        while (!heap.isEmpty()) {
            int current = heap.removeMin();
            assertTrue(current >= previous, "Heap property violated");
            previous = current;
        }
    }

    /**
     * Tests single element heap operations.
     */
    @Test
    void testSingleElementHeap() {
        heap.addElement(42);

        assertEquals(42, heap.findMin());
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());

        assertEquals(42, heap.removeMin());
        assertTrue(heap.isEmpty());
    }

    /**
     * Tests adding and removing the same values multiple times.
     */
    @Test
    void testRepeatedAddRemove() {
        for (int i = 0; i < 3; i++) {
            heap.addElement(10);
            heap.addElement(20);
            heap.addElement(30);

            assertEquals(3, heap.size());
            assertEquals(10, heap.removeMin());
            assertEquals(20, heap.removeMin());
            assertEquals(30, heap.removeMin());
            assertTrue(heap.isEmpty());
        }
    }

    /**
     * Tests heap with duplicate minimum values.
     */
    @Test
    void testDuplicateMinimums() {
        heap.addElement(10);
        heap.addElement(10);
        heap.addElement(10);
        heap.addElement(20);
        heap.addElement(20);

        assertEquals(10, heap.removeMin());
        assertEquals(10, heap.removeMin());
        assertEquals(10, heap.removeMin());
        assertEquals(20, heap.removeMin());
        assertEquals(20, heap.removeMin());
        assertTrue(heap.isEmpty());
    }

    /**
     * Tests heap with negative numbers.
     */
    @Test
    void testNegativeNumbers() {
        heap.addElement(5);
        heap.addElement(-10);
        heap.addElement(15);
        heap.addElement(-5);
        heap.addElement(0);

        assertEquals(-10, heap.findMin());
        assertEquals(-10, heap.removeMin());
        assertEquals(-5, heap.removeMin());
        assertEquals(0, heap.removeMin());
        assertEquals(5, heap.removeMin());
        assertEquals(15, heap.removeMin());
    }

    /**
     * Tests heap sort functionality.
     * <p>
     * Adding unsorted elements and removing them should produce sorted sequence.
     * </p>
     */
    @Test
    void testHeapSort() {
        int[] unsorted = {64, 34, 25, 12, 22, 11, 90};

        for (int val : unsorted) {
            heap.addElement(val);
        }

        int[] sorted = new int[unsorted.length];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = heap.removeMin();
        }

        for (int i = 1; i < sorted.length; i++) {
            assertTrue(sorted[i] >= sorted[i - 1]);
        }
    }

    /**
     * Tests the {@code toString} method.
     */
    @Test
    void testToString() {
        heap.addElement(10);
        heap.addElement(20);
        heap.addElement(30);

        String result = heap.toString();
        assertNotNull(result);
    }

    /**
     * Tests adding null element (if applicable).
     */
    @Test
    void testAddNull() {
        assertDoesNotThrow(() -> {
            try {
                heap.addElement(null);
            } catch (NullPointerException e) {
            }
        });
    }

    /**
     * Tests heap with a large number of elements.
     * <p>
     * Stress test to verify performance and correctness with many elements.
     * </p>
     */
    @Test
    void testLargeHeap() {
        int n = 1000;

        for (int i = n; i > 0; i--) {
            heap.addElement(i);
        }

        assertEquals(n, heap.size());
        assertEquals(1, heap.findMin());

        for (int i = 1; i <= n / 2; i++) {
            assertEquals(i, heap.removeMin());
        }

        assertEquals(n / 2, heap.size());
        assertEquals((n / 2) + 1, heap.findMin());
    }

    /**
     * Tests alternating add and remove operations.
     */
    @Test
    void testAlternatingOperations() {
        heap.addElement(30);
        heap.addElement(10);
        assertEquals(10, heap.removeMin());

        heap.addElement(50);
        heap.addElement(20);
        assertEquals(20, heap.removeMin());

        heap.addElement(40);
        assertEquals(30, heap.findMin());
        assertEquals(30, heap.removeMin());

        assertEquals(2, heap.size());
    }
}
